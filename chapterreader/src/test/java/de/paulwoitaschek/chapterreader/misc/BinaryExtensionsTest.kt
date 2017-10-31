package de.paulwoitaschek.chapterreader.misc

import com.google.common.truth.Truth.assertThat
import de.paulwoitaschek.chapterreader.ogg.DatatypeConverter
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.EOFException
import java.io.InputStream

class BinaryStreamExtensionsTest {

  private val binaryData = DatatypeConverter.parseHexBinary("6c6f6cffd7904df58a9e4e0c2582390eb2")
  private lateinit var stream: InputStream

  @Before
  fun setUp() {
    stream = ByteArrayInputStream(binaryData)
  }

  @Test
  fun startsWithExtension() {
    assertThat(binaryData.startsWith("lol".toByteArray())).isTrue()
    assertThat(binaryData.startsWith("test".toByteArray())).isFalse()
  }

  @Test
  fun toUIntExtension() {
    assertThat(2.toByte().toUInt()).isEqualTo(2)
    assertThat((-2).toByte().toUInt()).isEqualTo(254)
    assertThat(254.toByte().toUInt()).isEqualTo(254)
  }

  @Test
  fun toUlongExtension() {
    assertThat(2.toByte().toULong()).isEqualTo(2L)
    assertThat((-2).toByte().toULong()).isEqualTo(254L)
    assertThat(254.toByte().toULong()).isEqualTo(254L)
  }

  @Test
  fun readUint8() {
    assertThat(stream.readUInt8()).isEqualTo(0x6c)
    assertThat(stream.readUInt8()).isEqualTo(0x6f)
    assertThat(stream.readUInt8()).isEqualTo(0x6c)
    assertThat(stream.readUInt8()).isEqualTo(0xff)
  }

  @Test
  fun readLeUInt32() {
    assertThat(stream.readLeUInt32()).isEqualTo(0xff6c6f6cL)
    assertThat(stream.readLeUInt32()).isEqualTo(0xf54d90d7L)
  }

  @Test
  fun readLeInt32() {
    assertThat(stream.readLeInt32()).isEqualTo(-9670804)
    assertThat(stream.readLeInt32()).isEqualTo(-179466025)
  }

  @Test
  fun readLeInt64() {
    assertThat(stream.readLeInt64()).isEqualTo(-770800703832821908L)
    assertThat(stream.readLeInt64()).isEqualTo(1024993485835378314L)
  }

  @Test
  fun readBytes() {
    assertThat(stream.readAmountOfBytes(4)).isEqualTo(DatatypeConverter.parseHexBinary("6c6f6cff"))
    assertThat(stream.readAmountOfBytes(3)).isEqualTo(DatatypeConverter.parseHexBinary("d7904d"))
  }

  @Test
  fun skipBytes() {
    stream.skipBytes(10)
    assertThat(stream.readUInt8()).isEqualTo(0x4e)
  }

  @Test(expected = EOFException::class)
  fun endOfBinaryStreamException() {
    stream.readAmountOfBytes(100)
  }
}
