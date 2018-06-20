package log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
	class SubDirectoriesAndSize {
		final public long size;
		final public List<File> subDirectories;
		public SubDirectoriesAndSize(final long totalSize, final List<File> theSubDirs) {
			size = totalSize;
			subDirectories = Collections.unmodifiableList(theSubDirs);
		}
	}

	private ConcurrentHashMap<String, MyFile> map = new ConcurrentHashMap<>();

	private SubDirectoriesAndSize getSubDirs(final File file) {
		long total = 0;
		final List<File> subDirectories = new ArrayList<File>();
		if (file.isDirectory()) {
			final File[] children = file.listFiles();
			if (children != null)
				for (final File child : children) {
					map.put(child.getPath(), new MyFile(child));
					if (child.isFile())
						total+=child.length();
					else
						subDirectories.add(child);
				}
		}
		return new SubDirectoriesAndSize(total,subDirectories);
	}

	public long visitDir(final File file)
			throws InterruptedException, ExecutionException, TimeoutException {
		map.clear();
		long total = 0;
		final ExecutorService service = Executors.newFixedThreadPool(100);
		try {
			final List<File> directories = new ArrayList<File>();
			directories.add(file);
			while (!directories.isEmpty()) {
				final List<Future<SubDirectoriesAndSize>> partialResults = new ArrayList<Future<SubDirectoriesAndSize>>();
				for (final File directory : directories) {
					partialResults.add(service.submit(new Callable<SubDirectoriesAndSize>() {
						public SubDirectoriesAndSize call() {
							return getSubDirs(directory);
						}
					}));
				}
				directories.clear();
				for (final Future<SubDirectoriesAndSize> partialResultFuture : partialResults) {
					final SubDirectoriesAndSize subDirectoriesAndSize = partialResultFuture.get(100, TimeUnit.SECONDS);
					directories.addAll(subDirectoriesAndSize.subDirectories);
					total += subDirectoriesAndSize.size;
				}
			}
		} finally {
			service.shutdown();
		}
		return total;
	}
	
	public ConcurrentHashMap<String, MyFile> getFilesInDir(final File file)
	{
		return map;
	}
}
