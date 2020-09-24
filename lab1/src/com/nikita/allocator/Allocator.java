package com.nikita.allocator;

import com.nikita.util.ArrayUtils;

public class Allocator {
    private int size;
    private byte[] buffer;

    private final static int DEFAULT_SIZE = 1024;

    private static boolean checkAllocatorSize(int size) {
        return size > Header.HEADER_SIZE && size % 4 == 0;
    }

    private boolean checkIndex(int index) {
        return index >= 0 && index % 4 == 0 && index < size - Header.HEADER_SIZE;
    }

    private boolean checkBlockSize(int size) {
        return size > 0 && size % 4 == 0 && size <= this.size - Header.HEADER_SIZE;
    }

    private Header getHeader(int index) {
        byte[] headerByteArray = ArrayUtils.splitByteArray(buffer, index, index + Header.HEADER_SIZE);
        return new Header(headerByteArray);
    }

    private void writeHeader(int index, Header header) {
        byte[] headerByteArray = header.toByteArray();
        ArrayUtils.insertByteArray(buffer, headerByteArray, index);
    }

    private void updateNextHeaderSizePrev(int index) {
        Header header = getHeader(index);
        int nextIndex = index + Header.HEADER_SIZE + header.getSize();
        if (nextIndex > size - Header.HEADER_SIZE) {
            return;
        }

        Header nextHeader = getHeader(nextIndex);
        nextHeader.setSizePrev(header.getSize());

        writeHeader(nextIndex, nextHeader);
    }

    // Split block into two parts
    private void splitBlock(Header header, int size, int splitIndex) throws InvalidIndexException {
        int splitSize = header.getSize() - size - Header.HEADER_SIZE;
        // New block
        Header splitHeader = new Header(splitSize, size);
        writeHeader(splitIndex, splitHeader);
        // Set found header new size
        header.setSize(size);
        // Update next header prev size (next to split new block)
        updateNextHeaderSizePrev(splitIndex);
        free(splitIndex);
    }

    public Allocator() {
        size = DEFAULT_SIZE;
        buffer = new byte[DEFAULT_SIZE];
        Header header = new Header(size - Header.HEADER_SIZE, 0);
        writeHeader(0, header);
    }

    public Allocator(int size) throws InvalidSizeException {
        if (!checkAllocatorSize(size)) {
            throw new InvalidSizeException();
        }
        this.size = size;
        buffer = new byte[DEFAULT_SIZE];
        Header header = new Header(size - Header.HEADER_SIZE, 0);
        writeHeader(0, header);
    }

    // Alloc memory for the new block
    public int alloc(int size) throws InvalidSizeException {
        if (!checkBlockSize(size)) {
            throw new InvalidSizeException();
        }

        // Get first header
        int index = 0;
        Header header = getHeader(index);

        // Iterate through blocks to find
        // free with sufficient size
        while (!header.isFree() || header.getSize() < size) {
            index += Header.HEADER_SIZE + header.getSize();
            if (index > this.size - Header.HEADER_SIZE - size) {
                return -1;
            }
            header = getHeader(index);
        }

        // Block can be divided into two blocks
        if (header.getSize() > size + Header.HEADER_SIZE) {
            // Split blocks into two
            int splitIndex = index + Header.HEADER_SIZE + size;
            try {
                splitBlock(header, size, splitIndex);
            } catch (InvalidIndexException err) {
                System.err.println(err);
            }
        }

        // Set header as occupied
        header.setFree(false);
        // Write header to buffer
        writeHeader(index, header);

        return index;
    }

    public int realloc(int index, int size) throws InvalidIndexException, InvalidSizeException {
        if (!checkIndex(index)) {
            throw new InvalidIndexException();
        }
        Header header = getHeader(index);

        // Realloc needs less size than block has now
        // and current block can be split
        if (size < header.getSize() - Header.HEADER_SIZE) {
            // Split blocks into two
            int splitIndex = index + Header.HEADER_SIZE + size;
            splitBlock(header, size, splitIndex);
            // Set header as occupied
            header.setFree(false);
            // Write header to buffer
            writeHeader(index, header);
        }
        // Realloc needs more size than block has now or less size
        // but remainder of the size isn't enough to create new block
        else if (size != header.getSize()) {
            byte[] byteArray = ArrayUtils.splitByteArray(
                buffer,
                index + Header.HEADER_SIZE,
                index + Header.HEADER_SIZE + header.getSize()
            );
            // Alloc new block
            int newIndex = alloc(size);
            write(newIndex, byteArray);
            // Free old block
            free(index);
            // Return new index
            return newIndex;
        }

        return index;
    }

    // Free the block
    public void free(int index) throws InvalidIndexException {
        if (!checkIndex(index)) {
            throw new InvalidIndexException();
        }

        // Get header
        Header header = getHeader(index);

        int nextIndex = index + Header.HEADER_SIZE + header.getSize();
        int prevIndex = index - Header.HEADER_SIZE - header.getSizePrev();

        // Concat with next free blocks
        while (nextIndex < size - Header.HEADER_SIZE) {
            Header nextHeader = getHeader(nextIndex);
            if (!nextHeader.isFree()) {
                break;
            }
            header.setSize(header.getSize() + Header.HEADER_SIZE + nextHeader.getSize());
            nextIndex += Header.HEADER_SIZE + nextHeader.getSize();
        }

        // Concat with previous free blocks
        while (prevIndex >= 0) {
            Header prevHeader = getHeader(prevIndex);
            if (!prevHeader.isFree()) {
                break;
            }
            // Change previous header size
            prevHeader.setSize(header.getSize() + Header.HEADER_SIZE + prevHeader.getSize());

            // Set previous header as a header
            header = prevHeader;
            // Set previous index as an index
            index = prevIndex;

            prevIndex -= Header.HEADER_SIZE + prevHeader.getSize();
        }

        // Set header as not occupied
        header.setFree(true);

        // Write header to buffer
        writeHeader(index, header);

        // Update next header previous size
        updateNextHeaderSizePrev(index);
    }

    // Dump current allocator state
    public String dump() {
        String dump = "";
        int index = 0;

        // Iterate through blocks
        while (index < size) {
            Header header = getHeader(index);
            dump += header.toString() + '\n';
            index += Header.HEADER_SIZE + header.getSize();
        }

        return dump;
    }

    // Write data to the block
    public void write(int index, byte[] byteArray) throws InvalidIndexException {
        if (!checkIndex(index)) {
            throw new InvalidIndexException();
        }
        ArrayUtils.insertByteArray(buffer, byteArray, index + Header.HEADER_SIZE);
    }

    // Read data from the block
    public byte[] read(int index) throws InvalidIndexException {
        if (!checkIndex(index)) {
            throw new InvalidIndexException();
        }
        Header header = getHeader(index);
        return ArrayUtils.splitByteArray(
            buffer,
            index + Header.HEADER_SIZE,
            index + Header.HEADER_SIZE + header.getSize()
        );
    }
}