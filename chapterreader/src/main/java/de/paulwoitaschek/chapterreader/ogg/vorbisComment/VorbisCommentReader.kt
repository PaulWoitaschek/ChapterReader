package de.paulwoitaschek.chapterreader.ogg.vorbisComment


import de.paulwoitaschek.chapterreader.misc.readAmountOfBytes
import de.paulwoitaschek.chapterreader.misc.readLeUInt32
import java.io.InputStream

internal object VorbisCommentReader {

  private val VORBIS_COMMENT_CHAPTER_TIME_REGEX = Regex("""(\d+):(\d+):(\d+).(\d+)""")

  /**
   * Reads vorbis comment according to [specification](https://xiph.org/vorbis/doc/v-comment.html)
   */
  fun readComment(stream: InputStream): VorbisComment {
    val vendorLength = stream.readLeUInt32()
    val vendor = stream.readAmountOfBytes(vendorLength.toInt()).toString(Charsets.UTF_8)
    val numberComments = stream.readLeUInt32()
    val comments = (1..numberComments).map {
      val length = stream.readLeUInt32()
      val comment = stream.readAmountOfBytes(length.toInt()).toString(Charsets.UTF_8)
      val parts = comment.split("=", limit = 2)
      if (parts.size != 2) throw VorbisCommentParseException("Expected TAG=value comment format")
      Pair(parts[0].toUpperCase(), parts[1])
    }.toMap()
    return VorbisComment(vendor, comments)
  }

  fun parseChapterTime(timeStr: String): Int? {
    val matchResult = VORBIS_COMMENT_CHAPTER_TIME_REGEX.matchEntire(timeStr)
      ?: return null
    val hours = matchResult.groups[1]!!.value.toInt()
    val minutes = hours * 60 + matchResult.groups[2]!!.value.toInt()
    val seconds = minutes * 60 + matchResult.groups[3]!!.value.toInt()
    return seconds * 1000 + matchResult.groups[4]!!.value.take(3).padEnd(3, '0').toInt()
  }
}

