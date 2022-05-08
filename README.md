# Song Recommender

A song recommendation engine build with Apache Kafka Streams and Scala

The project requires 

- Java 8 or Java 11 

- Scala 2.13 and sbt 1.5.2+ environment to run.

- kcat https://github.com/edenhill/kcat

## Getting Started

### To run this project

1. Start Kafka

    In the `docker/kafka` directory run `docker-compose up -d`

2. Start the kafka streams application 

    `sbt "runMain recommender.SongRecommender"`

3. Start a producer to produce messages to the input topic. A song id can be found in the `main/resources/data/spotify_songs.csv` file.

   `kcat -b localhost:9092 -t song-input-topic -K: -P << EOF
    1:songIdHere
    EOF`

4. Shutdown all containers

    `docker compose down`

5. The recommended song will be found in `main/resources/data/output/recommended_song.csv`

### Useful commands

 Use the following commands to get started with your project

- Compile: `sbt compile`
- Create a "fat" jar: `sbt assembly`
- Run tests: `sbt test`
- To install in local repo: `sbt publishLocal`

### Static Analysis Tools

#### Scalafmt

To ensure clean code, run scalafmt periodically. The scalafmt configuration is defined at <https://scalameta.org/scalafmt/docs/configuration.html>

For source files,

`sbt scalafmt`

For test files.

`sbt test:scalafmt`

#### Scalafix

To ensure clean code, run scalafix periodically. The scalafix rules are listed at <https://scalacenter.github.io/scalafix/docs/rules/overview.html>

For source files,

`sbt "scalafix RemoveUnused"`

For test files.

`sbt "test:scalafix RemoveUnused"`

### Testing

- `sbt "test:testOnly *your-test-name"` runs all tests of the name given (could be many projects)
- `sbt "testOnly *.project-name.*"` runs a project
- `sbt "testOnly recommender.MainAppTest"` runs the exact test given
