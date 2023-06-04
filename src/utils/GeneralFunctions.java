package utils;

public class GeneralFunctions {
    public static int getPeriod(String data) {
		String time = data.split(" ")[1];
        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);

        if (hour >= 6 && (hour <= 11 && minute <= 59))
            return 1;
        else if (hour >= 12 && (hour <= 17 && minute <= 59))
            return 2;
        else if (hour >= 18 && (hour <= 23 && minute <= 59))
            return 3;
        else if (hour >= 0 && (hour <= 5 && minute <= 59))
            return 4;

        return -1;
    }

	public static String getParsedDate(String date) {
		String[] dateSplit = date.split(" ");
		String[] dateSplit2 = dateSplit[0].split("-");
		return dateSplit2[2] + "/" + dateSplit2[1] + "/" + dateSplit2[0];
	}
}
