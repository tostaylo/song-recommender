package recommender

import java.time.Duration
import java.util.Properties
import java.io._

// import scala.util.{ Failure, Success, Try }

// import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{ KafkaStreams, StreamsConfig }

// run with: sbt "runMain recommender.SongRecommender"
object SongRecommender {

  implicit class CSVWrapper(val prod: Product) extends AnyVal {
    def toCSV() = prod
      .productIterator
      .map {
        case Some(value) => value
        case None        => ""
        case rest        => rest
      }
      .mkString(",")
  }

  def writeFile(filename: String, lines: List[String]): Unit = {
    val file = new File(filename)
    val bw = new BufferedWriter(new FileWriter(file))

    for (line <- lines)
      bw.write(s"${line}\n")

    bw.close()
  }

  def main(args: Array[String]): Unit = {
    import Serdes._

    val props: Properties = {
      val p = new Properties()
      p.put(StreamsConfig.APPLICATION_ID_CONFIG, "song-recommender-application")
      p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
      p
    }

    val builder: StreamsBuilder = new StreamsBuilder
    val textLines: KStream[String, String] =
      builder.stream[String, String]("song-input-topic")

    val rawSongs = Song
      .readFromCSVFile(
        "src/main/resources/data/spotify_songs.csv"
      )

    val csvHeader = rawSongs(0)

    // the first song is the top row of the csv
    val songs = rawSongs
      .drop(1)

    textLines.foreach { (count, songId) =>
      writeFile(
        "src/main/resources/data/input-topics.log.txt",
        List(s"count: ${count} | id: ${songId}")
      )

      val foundSongs = songs.filter(_.id == songId)
      if (foundSongs.length > 0) {

        val foundSong = foundSongs(0)

        val sameGenreSongs =
          songs.filter(song =>
            song.playlistGenre == foundSong.playlistGenre && song.playlistSubGenre == foundSong.playlistSubGenre
          )

        // If this is accurate the first song should always be the input song since there should be a difference of 0 in attribute values
        val similarSongs =
          foundSong
            .rankSimilarSongs(sameGenreSongs)
            .filter(_._1 != foundSong.id)

        val recommendedSong =
          sameGenreSongs.find(_.id == similarSongs(0)._1).get

        writeFile(
          "src/main/resources/data/recommended_song.csv",
          List(csvHeader.toCSV(), recommendedSong.toCSV())
        )
        writeFile(
          "src/main/resources/data/found_song.csv",
          List(csvHeader.toCSV(), foundSong.toCSV())
        )
      }
      else {
        // Error Logging can be monitored by cloud architecture
        writeFile(
          "src/main/resources/data/errors.log.txt",
          List(s"Song id: ${songId} was not found")
        )
      }
    }

    val streams: KafkaStreams = new KafkaStreams(builder.build(), props)
    streams.start()

    sys.ShutdownHookThread {
      streams.close(Duration.ofSeconds(10))
    }
  }
}

// the key to this application will be building a  graph which will have the closest songs in similarity in the neighboring nodes.  But also a binary search tree for matching the song quickly to begin with. Will bloom filter help?

// Useful if we want to identify metrics for each genre which deviate from average song

// def attributeDiffsByGenre(
//     averageSongsBySubgenre: Map[String, Song],
//     averageSongAttributePairs: Map[String, Any]
//   ): Map[String, List[(String, Double)]] = averageSongsBySubgenre.map {
//   case (key, value) =>
//     val genre = key;
//     val subgenreAverageSong = value;

//     val subgenreAverageSongMap =
//       (subgenreAverageSong.productElementNames zip subgenreAverageSong.productIterator).toMap

//     var attributeDiffs: List[(String, Double)] = List()

//     averageSongAttributePairs.foreach { kv =>
//       val key = kv._1
//       val vl = kv._2
//       val subgenreAttributeVal = subgenreAverageSongMap(key)

//       if (subgenreAttributeVal != "") {
//         val diff =
//           (subgenreAttributeVal.toString().toDouble - vl
//             .toString()
//             .toDouble).abs

//         attributeDiffs = attributeDiffs.appended((key, diff))
//       }

//     }

//     (genre, attributeDiffs.sortBy(_._2));
// }

// val averageSong =
//   songs.reduce(_ |+| _).average(songs.length.toDouble)

// val averageSongAttributePairs =
//   (averageSong.productElementNames zip averageSong.productIterator).toMap

// val averageSongsBySubgenre =
//   songs
//     .groupBy(_.playlistSubGenre)
//     .map {
//       case (subgenre, songs) =>
//         (subgenre, songs.reduce(_ |+| _).average(songs.length.toDouble))
//     }
