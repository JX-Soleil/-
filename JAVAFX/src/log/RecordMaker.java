package log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import application.MyFile;

public class RecordMaker {

    public static final String fileName = "C:\\";
    private ConcurrentHashMap<String, MyFile> map = new ConcurrentHashMap<>();

//    public ConcurrentHashMap<String, MyFile> getMap()
//    {
//    	return map;
//    }
//    
    private List<File> getSubDirs(final File file) {
        final List<File> subDirectories = new ArrayList<File>();
        if (file.isDirectory()) {
            final File[] children = file.listFiles();
            if (children != null)
                for (final File child : children) {
                	map.put(child.getPath(), new MyFile(child));
                    if(child.isDirectory())
                        subDirectories.add(child);
                }
        }
        return subDirectories;
    }

    public ConcurrentHashMap<String, MyFile> getFilesInDir(final File file)
            throws InterruptedException, ExecutionException, TimeoutException {
    	map.clear();
        final ExecutorService service = Executors.newFixedThreadPool(100);
        try {
            final List<File> directories = new ArrayList<File>();
            directories.add(file);
            while (!directories.isEmpty()) {
                final List<Future<List<File>>> partialResults = new ArrayList<Future<List<File>>>();
                for (final File directory : directories) {
                    partialResults.add(service
                            .submit(new Callable<List<File>>() {
                                public List<File> call() {
                                    return getSubDirs(directory);
                                }
                            }));
                }
                directories.clear();
                for (final Future<List<File>> partialResultFuture : partialResults) {
                    final List<File> subDirectoriesAndSize = partialResultFuture
                            .get(100, TimeUnit.SECONDS);
                    directories.addAll(subDirectoriesAndSize);        
                }
            }
        } finally {
            service.shutdown();
        }
        return map;
    }

//    public static void main(final String[] args) throws InterruptedException,
//            ExecutionException, TimeoutException {
//        final long start = System.nanoTime();
//        RecordMaker recordMaker= new RecordMaker();
//        recordMaker.getFilesInDir(new File(fileName));
//        final long end = System.nanoTime();
////        for(Entry<String, MyFile> entry : recordMaker.map.entrySet())
////        {
////        	System.out.println(entry);
////        }
//        System.out.println("Time taken: " + (end - start) / 1.0e9);
//    }
}
