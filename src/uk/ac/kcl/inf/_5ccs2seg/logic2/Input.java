package uk.ac.kcl.inf._5ccs2seg.logic;


public class Input {
	
	/**
	 * @param args, mcp
	 * @author John Murray, Adrian Bocai for Team Dijkstra
	 * @see Needs some serious cleanup and need to move code into separate
	 *      methods but it validates all correct options and complains if
	 *      there's any mistakes in arguments given
	 */

	public static void ValidateInput(String[] args, MasterControlProgram mcp) {
		
		if (args.length == 0) {
			mcp.setGui(true);
		}

		for (int i = 0; i < args.length; i++) {
			String test = args[i];

			if (test.equals("-gui")) {
				mcp.setGui(true);
			} else if (test.equals("-multi")) {
				mcp.getArgumentOrder().add(args[i]);
			} else if (test.equals("-explore")) {
				mcp.getArgumentOrder().add(args[i]);
			} else if (test.equals("-solo")) {
				mcp.getArgumentOrder().add(args[i]);
			} else if (test.equals("-map")) {
				mcp.getArgumentOrder().add(args[i]);

				if (i + 1 < args.length) {
					String checkNext = args[i + 1];

					if (checkNext.charAt(0) != '-') {
						mcp.getMapOutputNames().add(checkNext);
						mcp.setNumberOfMaps(mcp.getNumberOfMaps() + 1);
						i++;
					} else {
						mcp.setNumberOfMaps(mcp.getNumberOfMaps() + 1);
						mcp.getMapOutputNames().add("output" + mcp.getNumberOfMaps());
					}
				} else {
					mcp.setNumberOfMaps(mcp.getNumberOfMaps() + 1);
					mcp.getMapOutputNames().add("output" + mcp.getNumberOfMaps());
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

					mcp.getArgumentOrder().add(args[i]);
					i = i + 4;

				}
			}

			else {
				System.out.println(args[i]
						+ " is not a valid commandline option");
				System.exit(0);
			}

		}

	}

}
