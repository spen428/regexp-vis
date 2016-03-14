package controller;

import java.util.List;

import model.AutomatonTransition;

public class StringUtils {
    /**
     * For a list of transitions, turn that in to a string in natural English,
     * e.g. [a, b, c, d] into "a, b, c and d".
     *
     * @param list The list of transitions
     * @return A string describing the list in natural English
     */
    public static String transitionListToEnglish(
            List<AutomatonTransition> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i).getData().toString());
            if (list.size() != 2 && i < list.size() - 1) {
                builder.append(",");
            }

            if (i == list.size() - 2) {
                /* Penultimate */
                builder.append(" and ");
            } else {
                builder.append(" ");
            }
        }

        return builder.toString();
    }
}
