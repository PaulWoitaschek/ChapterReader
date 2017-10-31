package de.paulwoitaschek.chapterreader.matroska

import com.google.common.truth.Truth.assertThat
import de.paulwoitaschek.chapterreader.NoOpLogger
import org.junit.Test

class ReadAsMatroskaChaptersTest {

  private val readMatroskaChapters = ReadAsMatroskaChapters(NoOpLogger)

  @Test
  fun readMatroskaChaptersTest() {
    val actual = readMatroskaChapters.read(MatroskaTestFileProvider.testFile)
    assertThat(actual).isEqualTo(MatroskaTestFileProvider.testFileMatroskaChapters)
  }
}
