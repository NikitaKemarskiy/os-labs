package com.nikita.allocator;

import com.nikita.util.ArrayUtils;

public class Allocator {
    private int size;
    public byte[] buffer;

    private final static int DEFAULT_SIZE = PageHeader.PAGE_TOTAL_SIZE * 10;

//    private boolean checkIndex(int index) {
//        return index >= 0 && index % 4 == 0 && index < size - BlockHeader.BLOCK_HEADER_SIZE;
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

    private int getPageFreeBlockIndex(int index) {
        PageHeader pageHeader = getPageHeader(index);
        PageType pageType = pageHeader.getPageType();

        int blockHeaderIndex = index + PageHeader.PAGE_HEADER_SIZE;
        while (blockHeaderIndex < index + PageHeader.PAGE_TOTAL_SIZE) {
            BlockHeader blockHeader = getBlockHeader(blockHeaderIndex);
            if (blockHeader.isFree()) {
                return blockHeaderIndex;
            }
            blockHeaderIndex += BlockHeader.BLOCK_HEADER_SIZE + pageType.getSize();
        }
        return -1;
    }

    private BlockHeader getBlockHeader(int index) {
        byte[] blockHeaderByteArray = ArrayUtils.splitByteArray(buffer, index, index + BlockHeader.BLOCK_HEADER_SIZE);
        return new BlockHeader(blockHeaderByteArray);
    }

    private int getValidSize(int size) {
        return size % PageHeader.PAGE_TOTAL_SIZE == 0
            ? size
            : PageHeader.PAGE_TOTAL_SIZE * (size / PageHeader.PAGE_TOTAL_SIZE + 1);
    }

    private void writePageHeader(int index, PageHeader pageHeader) {
        byte[] pageHeaderByteArray = pageHeader.toByteArray();
        ArrayUtils.insertByteArray(buffer, pageHeaderByteArray, index);
    }

    private void writeBlockHeader(int index, BlockHeader blockHeader) {
        byte[] blockHeaderByteArray = blockHeader.toByteArray();
        ArrayUtils.insertByteArray(buffer, blockHeaderByteArray, index);
    }

    private void initPages() {
        PageHeader pageHeader = new PageHeader();
        byte[] pageHeaderByteArray = pageHeader.toByteArray();
        int index = 0;
        while (index < buffer.length) {
            ArrayUtils.insertByteArray(buffer, pageHeaderByteArray, index);
            index += PageHeader.PAGE_TOTAL_SIZE;
        }
    }

    private void initPage(int index, PageHeader pageHeader) {
        PageType pageType = pageHeader.getPageType();
        writePageHeader(index, pageHeader);
        BlockHeader blockHeader = new BlockHeader();
        int blockHeaderIndex = index + PageHeader.PAGE_HEADER_SIZE;
        while (blockHeaderIndex < index + PageHeader.PAGE_TOTAL_SIZE) {
            writeBlockHeader(blockHeaderIndex, blockHeader);
            blockHeaderIndex += BlockHeader.BLOCK_HEADER_SIZE + pageType.getSize();
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
        PageType pageType = PageHeader.getPageTypeBySize(size);

        // Get first page header
        int pageIndex = 0;

        // Iterate through pages to find
        // free with sufficient blocks size type
        int index = -1;
        while (index == -1 && pageIndex < this.size - PageHeader.PAGE_HEADER_SIZE) {
            PageHeader pageHeader = getPageHeader(pageIndex);
            PageType currentPageType = pageHeader.getPageType();

            // Current page type matches needed type
            if (currentPageType == pageType) {
                int freeBlockIndex = getPageFreeBlockIndex(pageIndex);
                index = freeBlockIndex >= 0
                    ? freeBlockIndex
                    : index;
            }
            // Current page type is empty
            else if (currentPageType == PageType.EMPTY) {
                pageHeader.setPageType(pageType);
                initPage(pageIndex, pageHeader);
                index = pageIndex + PageHeader.PAGE_HEADER_SIZE;
            }

            pageIndex += PageHeader.PAGE_TOTAL_SIZE;
        }

        if (index >= 0) {
            BlockHeader blockHeader = getBlockHeader(index);
            blockHeader.setFree(false);
            writeBlockHeader(index, blockHeader);
        }

        return index;
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