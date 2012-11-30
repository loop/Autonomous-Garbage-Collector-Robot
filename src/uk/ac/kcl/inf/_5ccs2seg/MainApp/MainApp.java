package uk.ac.kcl.inf._5ccs2seg.MainApp;

import java.util.LinkedList;
import java.util.List;

public class MainApp {

	static boolean gui = false;
	static int numberOfMaps = 0;
	static List<String> argumentOrder = new LinkedList<String>();
	static List<String> mapOutputNames = new LinkedList<String>();
	
	static Bot cleaner1 = new Bot(0, true);

	/**
	 * @param args
	 * @author John Murray
	 * @see Needs some serious cleanup and need to move code into separate
	 *      methods but it validates all correct options and complains if
	 *      there's any mistakes in arguments given
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			gui = true;
		}

		for (int i = 0; i < args.length; i++) {
			String test = args[i];

			if (test.equals("-multi")) {
				argumentOrder.add(args[i]);
				cleaner1.setMode(true);
			} else if (test.equals("-explore")) {
				argumentOrder.add(args[i]);
				cleaner1.explore();
			} else if (test.equals("-solo")) {
				argumentOrder.add(args[i]);
				cleaner1.setMode(false);
			} else if (test.equals("-map")) {
				argumentOrder.add(args[i]);

				if (i + 1 < args.length) {
					String checkNext = args[i + 1];

					if (checkNext.charAt(0) != '-') {
						mapOutputNames.add(checkNext);
						numberOfMaps++;
						i++;
						cleaner1.map(checkNext);
					} else {
						numberOfMaps++;
						mapOutputNames.add("output" + numberOfMaps);
						cleaner1.map(null);
					}
				} else {
					numberOfMaps++;
					mapOutputNames.add("output" + numberOfMaps);
					cleaner1.map(null);
				}

			} else if (test.equals("-collect")) {
				if (i + 4 < args.length) {
					String checkX1 = args[i + 1];
					String checkY1 = args[i + 2];
					String checkX2 = args[i + 3];
					String checkY2 = args[i + 4];

					for (int j = 0; j < checkX1.length(); j++) {
						switch (checkX1.charAt(j)) {
						case '0':
							break;
						case '1':
							break;
						case '2':
							break;
						case '3':
							break;
						case '4':
							break;
						case '5':
							break;
						case '6':
							break;
						case '7':
							break;
						case '8':
							break;
						case '9':
							break;
						case '-':
							break;
						case '.':
							break;
						default:
							System.out.println(checkX1
									+ " is not a valid value for x1");
							System.exit(0);
						}
					}
					for (int j = 0; j < checkY1.length(); j++) {
						switch (checkY1.charAt(j)) {
						case '0':
							break;
						case '1':
							break;
						case '2':
							break;
						case '3':
							break;
						case '4':
							break;
						case '5':
							break;
						case '6':
							break;
						case '7':
							break;
						case '8':
							break;
						case '9':
							break;
						case '-':
							break;
						case '.':
							break;
						default:
							System.out.println(checkY1
									+ " is not a valid value for y1");
							System.exit(0);
						}
					}
					for (int j = 0; j < checkX2.length(); j++) {
						switch (checkX2.charAt(j)) {
						case '0':
							break;
						case '1':
							break;
						case '2':
							break;
						case '3':
							break;
						case '4':
							break;
						case '5':
							break;
						case '6':
							break;
						case '7':
							break;
						case '8':
							break;
						case '9':
							break;
						case '-':
							break;
						case '.':
							break;
						default:
							System.out.println(checkX2
									+ "  is not a valid value for x2");
							System.exit(0);
						}
					}
					for (int j = 0; j < checkY2.length(); j++) {
						switch (checkY2.charAt(j)) {
						case '0':
							break;
						case '1':
							break;
						case '2':
							break;
						case '3':
							break;
						case '4':
							break;
						case '5':
							break;
						case '6':
							break;
						case '7':
							break;
						case '8':
							break;
						case '9':
							break;
						case '-':
							break;
						case '.':
							break;
						default:
							System.out.println(checkY2
									+ "  is not a valid value for y2");
							System.exit(0);
						}
					}
					cleaner1.collect(Integer.parseInt(checkX1), Integer.parseInt(checkY1), 
							Integer.parseInt(checkX2), Integer.parseInt(checkY2));
					argumentOrder.add(args[i]);
					i = i + 4;

				}
			}

			else {
				System.out.println(args[i]
						+ " is not a valid commandline option");
				System.exit(0);
			}

		}

		while (argumentOrder.size() > 0) {
			System.out.println(argumentOrder.remove(0));
		}

		while (mapOutputNames.size() > 0) {
			System.out.println(mapOutputNames.remove(0));
		}

		System.out.println("GUI value is: " + gui);

	}

}
