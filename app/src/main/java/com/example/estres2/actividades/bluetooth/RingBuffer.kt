package com.example.estres2.actividades.bluetooth

class RingBuffer<T>(capacity: Int) {
    // queue elements
    private val buffer: Array<T?> = arrayOfNulls<Any>(capacity) as Array<T?>
    private var count = 0 // number of elements on queue
    private var indexOut = 0 // index of first element of queue
    private var indexIn = 0 // index of next available slot
    val isEmpty: Boolean
        get() = count == 0
    val isFull: Boolean
        get() = count == buffer.size

    fun size(): Int {
        return count
    }

    fun clear() {
        count = 0
    }

    fun push(item: T) {
        if (count == buffer.size) {
            println("Ring buffer overflow")
        }
        buffer[indexIn] = item
        indexIn = (indexIn + 1) % buffer.size // wrap-around
        if (count++ == buffer.size) {
            count = buffer.size
        }
    }

    fun pop() {
        if (isEmpty) {
            println("Ring buffer pop underflow")
        }
        buffer[indexOut] = null // to help with garbage collection
        if (count-- == 0) {
            count = 0
        }
        indexOut = (indexOut + 1) % buffer.size // wrap-around
    }

    operator fun next(): T? {
        if (isEmpty) {
            println("Ring buffer next underflow")
        }
        return buffer[indexOut]
    }
}