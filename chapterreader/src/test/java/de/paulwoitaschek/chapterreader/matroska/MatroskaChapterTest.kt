package de.paulwoitaschek.chapterreader.matroska

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MatroskaChapterTest {

  @Test
  fun getName() {
    val chapter = MatroskaChapter(
      0L, listOf(
        MatroskaChapterName("Podczęść 1", setOf("pol")),
        MatroskaChapterName("Subpart 1", setOf("eng", "ger")),
        MatroskaChapterName("サブパート1", setOf("jpn"))
      ), listOf()
    )
    assertThat(chapter.name()).isEqualTo("Podczęść 1")
    assertThat(chapter.name(listOf("ger", "jpn"))).isEqualTo("Subpart 1")
    assertThat(chapter.name(listOf("ind", "kac", "jpn", "eng"))).isEqualTo("サブパート1")
  }

  @Test
  fun noContentsLeadsToNull() {
    val actual = MatroskaChapter(0L, listOf(), listOf()).name()
    assertThat(actual).isNull()
  }
}
