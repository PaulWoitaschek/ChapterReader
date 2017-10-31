package de.paulwoitaschek.chapterreader.misc

import java.io.EOFException
import java.io.InputStream

fun InputStream.skipBytes(number: Int) = IOUtils.skipFully(this, number.toLong())

fun InputStream.readAmountOfBytes(number: Int): ByteArray {
  val buf = ByteArray(number)
  var index = 0
  while (index < number) {
    val read = read(buf, index, number - index)
    if (read == -1) throw EOFException()
    index += read
  }
  return buf
}

fun InputStream.readUInt8(): Int {
  val res = read()
  if (res == -1) throw EOFException()
  return res
}

fun InputStream.readLeUInt32(): Long {
  val buf = readAmountOfBytes(4)
  return buf[0].toULong() or
    (buf[1].toULong() shl 8) or
    (buf[2].toULong() shl 16) or
    (buf[3].toULong() shl 24)
}

fun InputStream.readLeInt32(): Int {
  val buf = readAmountOfBytes(4)
  return buf[0].toUInt() or
    (buf[1].toUInt() shl 8) or
    (buf[2].toUInt() shl 16) or
    (buf[3].toUInt() shl 24)
}

fun InputStream.readLeInt64(): Long {
  val buf = readAmountOfBytes(8)
  return buf[0].toULong() or
    (buf[1].toULong() shl 8) or
    (buf[2].toULong() shl 16) or
    (buf[3].toULong() shl 24) or
    (buf[4].toULong() shl 32) or
    (buf[5].toULong() shl 40) or
    (buf[6].toULong() shl 48) or
    (buf[7].toULong() shl 56)
}

fun ByteArray.startsWith(prefix: ByteArray): Boolean {
  if (this.size < prefix.size) return false
  return prefix.withIndex().all { (i, v) -> v == this[i] }
}

fun Byte.toUInt(): Int = toInt() and 0xFF
fun Byte.toULong() = toLong() and 0xFFL

