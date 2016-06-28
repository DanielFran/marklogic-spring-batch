package com.marklogic.spring.batch.item;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.document.DocumentWriteSet;
import com.marklogic.client.document.GenericDocumentManager;
import com.marklogic.client.io.FileHandle;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

import java.io.File;
import java.net.URI;
import java.util.List;

public class MarkLogicFileItemWriter extends AbstractDocumentWriter implements ItemWriter<FileHandle> {

    private DatabaseClient client;

    private GenericDocumentManager docMgr;

    public MarkLogicFileItemWriter(DatabaseClient databaseClient) {
        this.client = databaseClient;
    }

    @Override
    public void open(ExecutionContext executionContext) {
        docMgr = client.newDocumentManager();
    }

    @Override
    public void close() {
        client.release();
    }

    @Override
    public void write(List<? extends FileHandle> items) throws Exception {
        DocumentWriteSet batch = docMgr.newWriteSet();
        for (FileHandle item : items) {
            batch.add(transformFileAbsolutePathToUri(item.get()), buildMetadata(), item);
        }
        docMgr.write(batch);
    }

    public String transformFileAbsolutePathToUri(File file) {
        String absolutePath = file.getAbsolutePath();
        String uri = absolutePath.replace("\\", "/");
        return uri;
    }
}
