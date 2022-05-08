package recommender
import recommender.testutils.{ StandardTest }

class SongRecommenderTest extends StandardTest {

  "A Song" when {
    val inputSong = Song(
      "0",
      "1",
      "2",
      "3",
      "4",
      "5",
      "6",
      "7",
      "8",
      "9",
      "10",
      "11",
      "12",
      "13",
      "14",
      "15",
      "16",
      "17",
      "18",
      "19",
      "20",
      "21",
      "22"
    )

    val storedSongs = List(
      // 1 off
      Song(
        "1",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "22",
        "22"
      ),
      // 8 off
      Song(
        "2",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "20",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22"
      ),
      // 3 off
      Song(
        "3",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "12",
        "13",
        "17",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22"
      ),
      //4 off
      Song(
        "4",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "12",
        "14",
        "15",
        "16",
        "17",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22"
      )
    )

    "instantiated" should {
      "have correct properties" in {
        inputSong.id should be("0")
        inputSong.duration should be("22")
      }
    }

    "ranksSimilarSongs" should {
      "have correct order " in {
        val ranked = inputSong.rankSimilarSongs(storedSongs)

        ranked(0)._1 should be("1")
        ranked(1)._1 should be("3")
        ranked(2)._1 should be("4")
        ranked(3)._1 should be("2")
      }
    }
  }
}
