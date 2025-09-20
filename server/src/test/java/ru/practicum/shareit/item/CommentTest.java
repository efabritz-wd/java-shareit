package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;

public class CommentTest {
    @Test
    void testCommentEquality() {
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("text");

        Comment comment2 = new Comment();
        comment2.setId(1L);
        comment2.setText("1text");

        Comment comment3 = new Comment();
        comment3.setId(1L);
        comment3.setText("text");

        Assertions.assertTrue(comment1.equals(comment1));
        Assertions.assertFalse(comment2.equals(comment1));
        Assertions.assertFalse(comment2.equals(new Comment()));
        Assertions.assertFalse(comment3.equals(comment1));
    }

    @Test
    void testHashCode() {
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("text");

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("1text");

        Assertions.assertNotEquals(comment2.hashCode(), comment1.hashCode());
    }
}
