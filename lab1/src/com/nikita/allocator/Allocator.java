package com.nikita.allocator;

import com.nikita.util.ArrayUtils;

public class Allocator {
    private int size;
    private byte[] buffer;
    private int startUnallocIndex;

    private final static int DEFAULT_SIZE = 1024;

    private static boolean checkAllocatorSize(int size) {
        return size > Header.HEADER_SIZE && size % 4 == 0;
    }
    private static boolean checkIndex(int index) {
        return index >= 0 && index % 4 == 0;
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
        // Update start unalloc index
        updateStartUnallocIndex(index + Header.HEADER_SIZE + header.getSize());
    }

    private void updateNextHeaderSizePrev(int index) {
        Header header = getHeader(index);
        int nextIndex = index + Header.HEADER_SIZE + header.getSize();
        if (startUnallocIndex <= nextIndex || nextIndex > size - Header.HEADER_SIZE) {
            return;
        }

        Header nextHeader = getHeader(nextIndex);
        nextHeader.setSizePrev(header.getSize());

        writeHeader(nextIndex, nextHeader);
    }

    private void updateStartUnallocIndex(int newStartUnallocIndex) {
        startUnallocIndex = startUnallocIndex < newStartUnallocIndex
            ? newStartUnallocIndex
            : startUnallocIndex;
    }

    public Allocator() {
        size = DEFAULT_SIZE;
        buffer = new byte[DEFAULT_SIZE];
        startUnallocIndex = 0;
    }

    public Allocator(int size) throws InvalidSizeException {
        if (!checkAllocatorSize(size)) {
            throw new InvalidSizeException();
        }
        this.size = size;
        buffer = new byte[DEFAULT_SIZE];
        startUnallocIndex = 0;
    }

    public int alloc(int size) throws InvalidSizeException {
        if (!checkBlockSize(size)) {
            throw new InvalidSizeException();
        }

        // Get first header
        int index = 0;
        Header header = startUnallocIndex > 0
            ? getHeader(index)
            : new Header(size, 0);

        // Iterate through blocks to find
        // free with sufficient size
        while (startUnallocIndex > index && (!header.isFree() || header.getSize() < size)) {
            index += Header.HEADER_SIZE + header.getSize();
            if (index > this.size - Header.HEADER_SIZE - size) {
                return -1;
            }
            header = startUnallocIndex <= index
                ? new Header(size, header.getSize())
                : getHeader(index);
        }

        // Block can be divided into two blocks
        if (header.getSize() > size + Header.HEADER_SIZE) {
            // Split blocks into two
            int splitIndex = index + Header.HEADER_SIZE + size;
            int splitSize = header.getSize() - size - Header.HEADER_SIZE;
            // New block
            Block splitBlock = new Block(splitSize, size);
            ArrayUtils.insertByteArray(buffer, splitBlock.toByteArray(), splitIndex);
            // Set found header new size
            header.setSize(size);
            // Update next header prev size (next to split new block)
            updateNextHeaderSizePrev(splitIndex);
        }

        // Set header as occupied
        header.setFree(false);

        // Write header to buffer
        writeHeader(index, header);

        return index;
    }

    public int realloc(int index, int size) throws InvalidIndexException {
        if (!checkIndex(index)) {
            throw new InvalidIndexException();
        }
        Header header = getHeader(index);

        return index;
    }

    public void free(int index) throws InvalidIndexException {
        if (!checkIndex(index)) {
            throw new InvalidIndexException();
        }

        // Get header
        Header header = getHeader(index);

        int nextIndex = index + Header.HEADER_SIZE + header.getSize();
        int prevIndex = index - Header.HEADER_SIZE - header.getSizePrev();

        // Concat with next free blocks
        while (nextIndex < startUnallocIndex && nextIndex < size - Header.HEADER_SIZE) {
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

    public String dump() {
        String dump = "";
        int index = 0;
        int emptyBytes = size;

        // Iterate through blocks
        while (index < startUnallocIndex) {
            Header header = getHeader(index);
            emptyBytes -= Header.HEADER_SIZE + header.getSize();
            dump += header.toString() + '\n';
            index += Header.HEADER_SIZE + header.getSize();
        }

        dump += String.format("<Empty %d bytes>", emptyBytes);

        return dump;
    }

    public void write(int index, byte[] byteArray) {
        //...
    }

    public byte[] read(int index) {
        return null;
    }
}