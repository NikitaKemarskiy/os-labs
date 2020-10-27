package com.nikita.allocator;

import com.nikita.util.ArrayUtils;

public class Allocator {
    private int size;
    public byte[] buffer;

    private final static int DEFAULT_SIZE = PageHeader.PAGE_TOTAL_SIZE * 10;

//    private boolean checkIndex(int index) {
//        return index >= 0 && index % 4 == 0 && index < size - BlockHeader.BLOCK_HEADER_SIZE;
//    }
//
//    private boolean checkBlockSize(int size) {
//        int pagesNumber = this.size / PageHeader.PAGE_TOTAL_SIZE;
//        return
//            size > 0 &&
//            size <= this.size - pagesNumber * PageHeader.PAGE_HEADER_SIZE - pagesNumber * BlockHeader.BLOCK_HEADER_SIZE;
//    }

    private int getPageHeaderIndex(int index) {
        int pagesNumber = index / PageHeader.PAGE_TOTAL_SIZE;
        return pagesNumber * PageHeader.PAGE_TOTAL_SIZE;
    }

    private PageHeader getPageHeader(int index) {
        int pageHeaderIndex = getPageHeaderIndex(index);
        byte[] pageHeaderByteArray = ArrayUtils.splitByteArray(buffer, pageHeaderIndex, pageHeaderIndex + PageHeader.PAGE_HEADER_SIZE);
        return new PageHeader(pageHeaderByteArray);
    }

//    private BlockHeader getHeader(int index) {
//        byte[] headerByteArray = ArrayUtils.splitByteArray(buffer, index, index + BlockHeader.BLOCK_HEADER_SIZE);
//        return new BlockHeader(headerByteArray);
//    }

    private int getValidSize(int size) {
        return size % PageHeader.PAGE_TOTAL_SIZE == 0
            ? size
            : PageHeader.PAGE_TOTAL_SIZE * (size / PageHeader.PAGE_TOTAL_SIZE + 1);
    }

//    private void writeHeader(int index, BlockHeader blockHeader) {
//        byte[] headerByteArray = blockHeader.toByteArray();
//        ArrayUtils.insertByteArray(buffer, headerByteArray, index);
//    }

    private void initPages() {
        PageHeader pageHeader = new PageHeader();
        byte[] pageHeaderByteArray = pageHeader.toByteArray();
        int index = 0;
        while (index < buffer.length) {
            ArrayUtils.insertByteArray(buffer, pageHeaderByteArray, index);
            index += PageHeader.PAGE_TOTAL_SIZE;
        }
    }

    public Allocator() {
        size = DEFAULT_SIZE;
        buffer = new byte[size];
        initPages();
    }

    public Allocator(int size) {
        this.size = getValidSize(size);
        buffer = new byte[this.size];
        initPages();
    }

    // Alloc memory for the new block
    public int alloc(int size) {
//        if (!checkBlockSize(size)) {
//            throw new InvalidSizeException();
//        }
//
//        // Get first header
//        int index = 0;
//        BlockHeader blockHeader = getHeader(index);
//
//        // Iterate through blocks to find
//        // free with sufficient size
//        while (!blockHeader.isFree() || blockHeader.getSize() < size) {
//            index += BlockHeader.HEADER_SIZE + blockHeader.getSize();
//            if (index > this.size - BlockHeader.HEADER_SIZE - size) {
//                return -1;
//            }
//            blockHeader = getHeader(index);
//        }
//
//        // Block can be divided into two blocks
//        if (blockHeader.getSize() > size + BlockHeader.HEADER_SIZE) {
//            // Split blocks into two
//            int splitIndex = index + BlockHeader.HEADER_SIZE + size;
//            try {
//                splitBlock(blockHeader, size, splitIndex);
//            } catch (InvalidIndexException err) {
//                System.err.println(err);
//            }
//        }
//
//        // Set header as occupied
//        blockHeader.setFree(false);
//        // Write header to buffer
//        writeHeader(index, blockHeader);
//
//        return index;
        return 0;
    }

    public int realloc(int index, int size) throws InvalidIndexException, InvalidSizeException {
//        if (!checkIndex(index)) {
//            throw new InvalidIndexException();
//        }
//        BlockHeader blockHeader = getHeader(index);
//
//        // Realloc needs less size than block has now
//        // and current block can be split
//        if (size < blockHeader.getSize() - BlockHeader.HEADER_SIZE) {
//            // Split blocks into two
//            int splitIndex = index + BlockHeader.HEADER_SIZE + size;
//            splitBlock(blockHeader, size, splitIndex);
//            // Set header as occupied
//            blockHeader.setFree(false);
//            // Write header to buffer
//            writeHeader(index, blockHeader);
//        }
//        // Realloc needs more size than block has now or less size
//        // but remainder of the size isn't enough to create new block
//        else if (size != blockHeader.getSize()) {
//            int dataSize = Math.min(size, blockHeader.getSize());
//            byte[] byteArray = ArrayUtils.splitByteArray(
//                buffer,
//                index + BlockHeader.HEADER_SIZE,
//                index + BlockHeader.HEADER_SIZE + dataSize
//            );
//
//            // Alloc new block
//            int newIndex = alloc(size);
//            System.out.println(newIndex + " " + byteArray.length);
//            write(newIndex, byteArray);
//
//            // Free old block
//            free(index);
//            // Return new index
//            return newIndex;
//        }
//
//        return index;
        return 0;
    }

    // Free the block
    public void free(int index) throws InvalidIndexException {
//        if (!checkIndex(index)) {
//            throw new InvalidIndexException();
//        }
//
//        // Get header
//        BlockHeader blockHeader = getHeader(index);
//
//        int nextIndex = index + BlockHeader.HEADER_SIZE + blockHeader.getSize();
//        int prevIndex = index - BlockHeader.HEADER_SIZE - blockHeader.getSizePrev();
//
//        // Concat with next free blocks
//        while (nextIndex < size - BlockHeader.HEADER_SIZE) {
//            BlockHeader nextBlockHeader = getHeader(nextIndex);
//            if (!nextBlockHeader.isFree()) {
//                break;
//            }
//            blockHeader.setSize(blockHeader.getSize() + BlockHeader.HEADER_SIZE + nextBlockHeader.getSize());
//            nextIndex += BlockHeader.HEADER_SIZE + nextBlockHeader.getSize();
//        }
//
//        // Concat with previous free blocks
//        while (prevIndex >= 0) {
//            BlockHeader prevBlockHeader = getHeader(prevIndex);
//            if (!prevBlockHeader.isFree()) {
//                break;
//            }
//            // Change previous header size
//            prevBlockHeader.setSize(blockHeader.getSize() + BlockHeader.HEADER_SIZE + prevBlockHeader.getSize());
//
//            // Set previous header as a header
//            blockHeader = prevBlockHeader;
//            // Set previous index as an index
//            index = prevIndex;
//
//            prevIndex -= BlockHeader.HEADER_SIZE + prevBlockHeader.getSize();
//        }
//
//        // Set header as not occupied
//        blockHeader.setFree(true);
//
//        // Write header to buffer
//        writeHeader(index, blockHeader);
//
//        // Update next header previous size
//        updateNextHeaderSizePrev(index);
    }

    // Dump current allocator state
    public String dump() {
        String dump = "";
        int index = 0;

        // Iterate through blocks
        while (index < size) {
            PageHeader pageHeader = getPageHeader(index);
            dump += pageHeader.toString() + '\n';
            index += PageHeader.PAGE_TOTAL_SIZE;
        }

        return dump;
    }

    // Write data to the block
    public void write(int index, byte[] byteArray) throws InvalidIndexException {
//        if (!checkIndex(index)) {
//            throw new InvalidIndexException();
//        }
//        ArrayUtils.insertByteArray(buffer, byteArray, index + BlockHeader.HEADER_SIZE);
    }

    // Read data from the block
    public byte[] read(int index) throws InvalidIndexException {
//        if (!checkIndex(index)) {
//            throw new InvalidIndexException();
//        }
//        BlockHeader blockHeader = getHeader(index);
//        return ArrayUtils.splitByteArray(
//            buffer,
//            index + BlockHeader.HEADER_SIZE,
//            index + BlockHeader.HEADER_SIZE + blockHeader.getSize()
//        );
        return null;
    }
}