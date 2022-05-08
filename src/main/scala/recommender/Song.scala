package recommender

import cats._
import cats.implicits._

import scala.io.Source

final case class Song(
    id: String,
    name: String,
    artist: String,
    popularity: String,
    albumId: String,
    albumName: String,
    albumReleaseDate: String,
    playlistName: String,
    playlistId: String,
    playlistGenre: String,
    playlistSubGenre: String,
    danceability: String,
    energy: String,
    key: String,
    loudness: String,
    mode: String,
    speechiness: String,
    acousticness: String,
    instrumentalness: String,
    liveness: String,
    valence: String,
    tempo: String,
    duration: String
  ) {

  def average(count: Double): Song = Song(
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    (danceability.toDouble / count).toString(),
    (energy.toDouble / count).toString(),
    (key.toDouble / count).toString(),
    (loudness.toDouble / count).toString(),
    (mode.toDouble / count).toString(),
    (speechiness.toDouble / count).toString(),
    (acousticness.toDouble / count).toString(),
    (instrumentalness.toDouble / count).toString(),
    (liveness.toDouble / count).toString(),
    (valence.toDouble / count).toString(),
    (tempo.toDouble / count).toString(),
    ""
  )

  def rankSimilarSongs(songs: List[Song]): List[(String, Double)] =
    songs
      .map { song =>
        val sum =
          (danceability.toDouble - song
            .danceability
            .toDouble).abs + (energy.toDouble - song
            .energy
            .toDouble).abs + (energy.toDouble - song
            .energy
            .toDouble).abs + (key.toDouble - song
            .key
            .toDouble).abs + (loudness.toDouble - song
            .loudness
            .toDouble).abs + (mode.toDouble - song
            .mode
            .toDouble).abs + (speechiness.toDouble - song
            .speechiness
            .toDouble).abs + (acousticness.toDouble - song
            .acousticness
            .toDouble).abs + (instrumentalness.toDouble - song
            .instrumentalness
            .toDouble).abs + (liveness.toDouble - song
            .liveness
            .toDouble).abs + (valence.toDouble - song
            .valence
            .toDouble).abs + (tempo.toDouble - song.tempo.toDouble).abs

        (song.id, sum)

      }
      .sortBy(_._2)
}

object Song {
  def apply(csvString: String): Option[Song] =
    try {
      val csvToList = csvString.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")
      if (csvToList.length != 23) {
        // need to print error to console here
        println(csvToList(1))
      }

      Some(
        Song(
          csvToList(0),
          csvToList(1),
          csvToList(2),
          csvToList(3),
          csvToList(4),
          csvToList(5),
          csvToList(6),
          csvToList(7),
          csvToList(8),
          csvToList(9),
          csvToList(10),
          csvToList(11),
          csvToList(12),
          csvToList(13),
          csvToList(14),
          csvToList(15),
          csvToList(16),
          csvToList(17),
          csvToList(18),
          csvToList(19),
          csvToList(20),
          csvToList(21),
          csvToList(22)
        )
      )

    }
    catch {
      case _: Throwable => None
    }

  def readFromCSVFile(fileName: String): List[Song] =
    Source
      .fromFile(fileName)
      .getLines()
      .collect { line =>
        Song.apply(line) match {
          case Some(song) => song
        }
      }
      .toList

  implicit val averageSongMonoid: Monoid[Song] =
    new Monoid[Song] {
      override def empty: Song = Song.apply("").get

      override def combine(x: Song, y: Song): Song =
        Song(
          "",
          "",
          "",
          "",
          "",
          "",
          "",
          "",
          "",
          "",
          "",
          (x.danceability.toDouble + y.danceability.toDouble).toString(),
          (x.energy.toDouble + y.energy.toDouble).toString(),
          (x.key.toDouble + y.key.toDouble).toString(),
          (x.loudness.toDouble + y.loudness.toDouble).toString(),
          (x.mode.toDouble + y.mode.toDouble).toString(),
          (x.speechiness.toDouble + y.speechiness.toDouble).toString(),
          (x.acousticness.toDouble + y.acousticness.toDouble).toString(),
          (x.instrumentalness.toDouble + y.instrumentalness.toDouble)
            .toString(),
          (x.liveness.toDouble + y.liveness.toDouble).toString(),
          (x.valence.toDouble + y.valence.toDouble).toString(),
          (x.tempo.toDouble + y.tempo.toDouble).toString(),
          ""
        )
    }
}
