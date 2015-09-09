package moe.mzry.ilmare.service.data;

import java.util.Comparator;

/**
 * Comparators for Message object
 */
public class MessageComparators {

    public static class MessageCreationTimeComparator implements Comparator<Message> {

        @Override
        public int compare(Message lhs, Message rhs) {
            // Sort by creation time, later one comes first
            return - lhs.getCreationTime().compareTo(rhs.getCreationTime());
        }
    }

    public static class MessageDistanceComparator implements Comparator<Message> {

        private final LocationSpec currentLocation;
        public MessageDistanceComparator(LocationSpec currentLocation) {
            this.currentLocation = currentLocation;
        }

        @Override
        public int compare(Message lhs, Message rhs) {
            return 0;
        }
    }
}
