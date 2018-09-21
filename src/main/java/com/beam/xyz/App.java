package com.beam.xyz;

import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.extensions.gcp.options.GcsOptions;
import org.apache.beam.sdk.extensions.gcp.storage.GcsCreateOptions;
import org.apache.beam.sdk.extensions.gcp.storage.GcsFileSystemRegistrar;
import org.apache.beam.sdk.extensions.gcp.storage.GcsResourceId;
import org.apache.beam.sdk.io.FileSystem;
import org.apache.beam.sdk.io.FileSystems;
import org.apache.beam.sdk.io.fs.CreateOptions;
import org.apache.beam.sdk.io.fs.ResourceId;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.util.GcsUtil;
import org.apache.beam.sdk.util.GcsUtil.GcsUtilFactory;
import org.apache.beam.sdk.util.gcsfs.GcsPath;
import org.apache.commons.compress.utils.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Example that fails GCS.
 */
public class App 
{
    static int MB_IN_BYTES = 1048576;

    public static void main( String[] args ) throws IOException {
        if (args.length < 3) {
            System.out.println("Please provide 3 arguments. Project, gsPath, upload buffer size in MB.");
            System.exit(-1);
        }
        String gcpProject = args[0];
        String gsPath = args[1];
        int uploadBufferSizeInBytes = Integer.valueOf(args[2]) * MB_IN_BYTES;
        System.out.println(gcpProject);
        System.out.println(gsPath);
        System.out.println(uploadBufferSizeInBytes);

        GcsOptions options = PipelineOptionsFactory.as(GcsOptions.class);

        options.setProject(gcpProject);
        options.setGcsUploadBufferSizeBytes(uploadBufferSizeInBytes);

        FileSystems.setDefaultPipelineOptions(options);
        GcsCreateOptions createOptions = GcsCreateOptions.builder().setGcsUploadBufferSizeBytes(uploadBufferSizeInBytes).setMimeType("plain/text").build();

        int i = 0;
        while (true) {
            i++;
            ResourceId id = FileSystems.matchNewResource(gsPath + "/file" + i + ".txt", false);

            WritableByteChannel channel = FileSystems.create(id, createOptions);
            FileInputStream in = new FileInputStream("file.txt");

            FileChannel inChannel = in.getChannel();
            final ByteBuffer buffer = ByteBuffer.allocateDirect(4 * uploadBufferSizeInBytes);

            while (inChannel.read(buffer) != -1) {
                buffer.flip();
                channel.write(buffer);
                buffer.compact();
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            channel.close();

            System.out.println("upload: " + i + " done.");
        }
    }
}
