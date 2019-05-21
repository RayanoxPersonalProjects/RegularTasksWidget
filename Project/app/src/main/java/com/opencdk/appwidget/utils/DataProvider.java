package com.opencdk.appwidget.utils;

import com.opencdk.appwidget.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DataProvider {

	public static final String[] NEWS_ARRAY = new String[] { "Activité premiere", "Une deuxieme tache",
			"Troisieme tache", "Quatrieme  tache", "5eme task",
			"Faire un nouveau test", "Ici aussi", "La un peu moins",
			"Et rebelotte !!!", "La on a bientot terminé en vrai", "La on a fini nan ?",
			"Ouii tu as raison" };

	public static int[] getRandomArray(int total) {
		int[] array = new int[total];
		for (int i = 0; i < total; i++) {
			array[i] = i;
		}
		Random random = new Random();
		int temp2;
		int end = total;
		for (int i = 0; i < total; i++) {
			int temp = random.nextInt(end);
			temp2 = array[temp];
			array[temp] = array[end - 1];
			array[end - 1] = temp2;
			end--;
		}

		return array;
	}

	public static String[] getRandomNewsArray(int count) {
		int[] randomArray = getRandomArray(count);
		String[] newsArray = new String[count];
		for (int i = 0; i < count; i++) {
			newsArray[i] = NEWS_ARRAY[randomArray[i]];
		}

		return newsArray;
	}

    /**
     * 模拟新闻数据
     *
     * @return
     */
    public static List<Task> getRandomNews() {
        List<Task> taskList = new ArrayList<Task>();

        final int newsCount = 10;
        Task task = new Task();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String dateString = null;
        // 随机新闻
        String[] newsArray = DataProvider.getRandomNewsArray(newsCount);
        // 随机新闻图片标签
        int[] randMarkArray = DataProvider.getRandomArray(newsCount);
        for (int i = 0; i < newsCount; i++) {
            task = new Task();
            task.setTitle(newsArray[i]);
            dateString = sdf.format(new Date());
            task.setDate(dateString);

            taskList.add(task);
        }

        return taskList;
    }

}
