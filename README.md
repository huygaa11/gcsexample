### 1. clone this repo

### 2. Create a big file called `file.txt` inside the `gcsexample` folder:



On linux: Using this [website](https://www.skorks.com/2010/03/how-to-quickly-generate-a-large-file-on-the-command-line-with-linux/), creating 64mb file:

```
cd gcsexample
dd if=/dev/zero of=file.txt count=65536 bs=1024
```

### 3. Run this command to write the file to GC bucket, with modifications to the arguments:
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

Deploy it on many computers and wait until it fails.
