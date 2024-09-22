package com.terabyte.mediastorage.model

class MemoryUsageModel {

    private var memoryUsageBytes: Long = 0 //memoryUsage in bytes
    var memoryUsage: Long = 0
        private set

    var type: String = DataType.B
        private set

    fun calculateMemoryUsage(bytes: ByteArray): String {
        memoryUsageBytes += bytes.size
        convertMemoryUsageBytesToUIView()
        return "$memoryUsage $type"
    }

    fun deleteFromMemoryUsage(deletedBytesSize: Int): String {
        memoryUsageBytes -= deletedBytesSize
        if(memoryUsageBytes<0) memoryUsageBytes = 0
        convertMemoryUsageBytesToUIView()
        return "$memoryUsage $type"
    }

    fun defaultValue(): String {
        return "$memoryUsage $type"
    }

    private fun convertMemoryUsageBytesToUIView() {
        if(memoryUsageBytes>1024*1024*1024) {
            type = DataType.GB
            memoryUsage = memoryUsageBytes / (1024*1024*1024)
        }
        else if(memoryUsageBytes>1024*1024) {
            type = DataType.MB
            memoryUsage = memoryUsageBytes / (1024*1024)
        }
        else if(memoryUsageBytes>1024) {
            type = DataType.KB
            memoryUsage = memoryUsageBytes / 1024
        }
        else {
            type = DataType.B
            memoryUsage = memoryUsageBytes
        }
    }

    object DataType {
        val B = "B"
        val KB = "KB"
        val MB = "MB"
        val GB = "GB"
    }
}