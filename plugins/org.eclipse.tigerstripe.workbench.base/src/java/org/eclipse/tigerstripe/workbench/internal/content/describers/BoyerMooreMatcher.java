package org.eclipse.tigerstripe.workbench.internal.content.describers;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class BoyerMooreMatcher {

	private final String pattern;
	private Map<Character, Integer> rmIndexes;

	public BoyerMooreMatcher(String pattern) {
		this.pattern = pattern;
		rmIndexes = preprocess(pattern);
	}
	
	public boolean contains(Reader reader) throws IOException {
		
		if (pattern.isEmpty()) {
			return false;
		}
		
		CharProvider text = new CharProvider(reader, pattern.length(), 2000);
		
		int n = pattern.length();

		int alignedAt = 0;
		while (!text.eof()) {

			for (int indexInPattern = n - 1; indexInPattern >= 0; indexInPattern--) {
				int indexInText = alignedAt + indexInPattern;
				char x = text.at(indexInText);
				
				if (x == -1) {
					return false;
				}
				
				char y = pattern.charAt(indexInPattern);

				if (x != y) {

					Integer r = rmIndexes.get(x);

					if (r == null) {
						alignedAt = indexInText + 1;
					}

					else {
						int shift = indexInText - (alignedAt + r);
						alignedAt += shift > 0 ? shift : 1;
					}

					break;
				} else if (indexInPattern == 0) {
					return true;
				}

			}
		}
		return false;
	}

	private static Map<Character, Integer> preprocess(
			String pattern) {
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		for (int i = pattern.length() - 1; i >= 0; i--) {
			char c = pattern.charAt(i);
			if (!map.containsKey(c))
				map.put(c, i);
		}

		return map;
	}
	
	private static class CharProvider {
		
		private static final char EOF = (char)-1;
		private final char[] backup; 
		private final char[] buffer;
		private int part = 0;
		private int readed;
		private final Reader reader;
		
		public CharProvider(Reader reader, int patternLength, int bufferSize) throws IOException {
			this.reader = reader;
			backup = new char[patternLength];
			buffer = new char[bufferSize];
			readed = reader.read(buffer);
		}
		
		public char at(int pos) throws IOException {
			
			if (pos < buffer.length * part) {
				int idx = pos - (buffer.length - backup.length);
				if (idx > backup.length - 1 || idx < 0) {
					return EOF;
				}
				return backup[idx];
			}
			
			if (pos > buffer.length * part + readed - 1) {
				
				if (readed >= backup.length) {
					System.arraycopy(buffer, readed - backup.length, backup, 0, backup.length);
				}
				
				readed = reader.read(buffer);
				if (readed <= 0) {
					return EOF;
				}
				++ part;
			}
			
			int idx = pos - part * buffer.length;
			if (idx > readed - 1 || idx < 0) {
				return EOF;
			}
			return buffer[idx];
		}
		
		public boolean eof() {
			return readed <= 0;
		}
		
	}
}
