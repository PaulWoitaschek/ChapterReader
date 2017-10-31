package de.paulwoitaschek.chapterreader.mp4

import de.paulwoitaschek.chapterreader.misc.toUInt
import java.io.File
import java.io.RandomAccessFile

/**
 * Reads chapters from the chpl atom
 */
internal object ChplReader {

  fun read(file: File): Map<Int, String> = RandomAccessFile(file, "r").use { raf ->

    val atoms = raf.atoms(listOf("moov", "udta"))

    val timeScale = readTimeScale(raf, atoms)
      ?: return emptyMap()

    val chplAtom = atoms.findAtom("moov", "udta", "chpl")
      ?: return emptyMap()
    raf.seek(chplAtom.position + 8)
    raf.skipBytes(8)
    val count = raf.readByte().toUInt()

    val map = HashMap<Int, String>(count)
    repeat(count) {
      val duration = raf.readUInt64() / timeScale / 10
      val titleSize = raf.readByte().toInt()

      val titleBytes = ByteArray(titleSize)
      raf.read(titleBytes)
      val title = String(titleBytes)

      map.put(duration.toInt(), title)
    }
    map
  }

  private fun readTimeScale(raf: RandomAccessFile, atoms: List<Mp4Atom>): Int? {
    val movieHeaderAtom = atoms.findAtom("moov", "mvhd")
      ?: return null
    raf.seek(movieHeaderAtom.position + 8)
    val version = raf.readByte().toInt()
    if (version != 0 && version != 1) return null

    val sizeFlags = 3
    val sizeCreationTime = if (version == 0) 4 else 8
    val sizeModificationTime = if (version == 0) 4 else 8

    raf.skipBytes(sizeFlags + sizeCreationTime + sizeModificationTime)
    return raf.readInt()
  }
}
