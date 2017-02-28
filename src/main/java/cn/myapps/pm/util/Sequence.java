package cn.myapps.pm.util;

import org.safehaus.uuid.UUIDGenerator;


public class Sequence {
	private final static int MAX_DEPTS_PER_LEVEL = 100;

	private final static int MAX_GROUP_PER_LEVEL = 100;

	private final static int MAX_PERSONS_PER_LEVEL = 1000;

	private static final int base = 100000;

	private static long millis = 0;

	private static long counter = 0;

	private static long old = 0;

	public final static int MAX_TOPIC_NUMBER = 1000000;

	public final static int MAX_TOPICCHILD_NUMBER = 1000;

	public final static int MAX_TITLE_COUNT = 10000;

	public final static int MAX_SHORTMESSAGE_NUMBER = 9999;

	private static char[] HandlerChars = { 'A', 'B', 'C' };

	private static char baseChar = 'A';

	private static final char maxChar = 'Z';

	private static Object obj = new Object();

	/**
	 * Get the base Sequence.
	 * 
	 * @return the Sequence
	 * @throws SequenceException
	 */
	public static synchronized String getSequence() throws SequenceException {
		UUIDGenerator gen = UUIDGenerator.getInstance();
		org.safehaus.uuid.UUID uuid = gen.generateTimeBasedUUID();

		String uuidStr = uuid.toString();
		String[] uuidParts = uuidStr.split("-");
		StringBuffer builder = new StringBuffer();
		builder.append(uuidParts[2]);
		builder.append("-");
		builder.append(uuidParts[1]);
		builder.append("-");
		builder.append(uuidParts[0]);
		builder.append("-");
		builder.append(uuidParts[3]);
		builder.append("-");
		builder.append(uuidParts[4]);

		return builder.toString();
	}

}