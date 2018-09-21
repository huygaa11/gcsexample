Create a big file called `file.txt` inside the `gcsexample` folder:

```cd gcsexample```

On mac:
```(mkfile -n 8g file.txt)```

For linux, try this [approach](https://unix.stackexchange.com/questions/269180/how-to-create-a-large-file-in-unix)

Run this command to write the file to GC bucket, with modifications to the arguments:
```
mvn install
mvn exec:java -Dexec.mainClass="com.beam.xyz.App" -Dorg.slf4j.simpleLogger.defaultLogLevel=debug \
-Dexec.args="<GCP_PROJECT> <GS_PATH> <CHUNK_SIZE_IN_MB>"
```

- argument 1: GCP project
- argument 2: GS path to write a file
- argument 3: chunk size in MB

For example, I was running it as

```
mvn exec:java -Dexec.mainClass="com.beam.xyz.App" -Dorg.slf4j.simpleLogger.defaultLogLevel=debug \
-Dexec.args="apache-beam-testing gs://batbat-storage/tmp 64"
```
