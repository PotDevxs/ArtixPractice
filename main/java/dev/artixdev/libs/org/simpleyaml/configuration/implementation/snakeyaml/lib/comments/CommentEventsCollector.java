package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.comments;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.CommentEvent;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.Event;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.parser.Parser;

public class CommentEventsCollector {
   private List<CommentLine> commentLineList;
   private final Queue<Event> eventSource;
   private final CommentType[] expectedCommentTypes;

   public CommentEventsCollector(final Parser parser, CommentType... expectedCommentTypes) {
      this.eventSource = new AbstractQueue<Event>() {
         public boolean offer(Event e) {
            throw new UnsupportedOperationException();
         }

         public Event poll() {
            return parser.getEvent();
         }

         public Event peek() {
            return parser.peekEvent();
         }

         public Iterator<Event> iterator() {
            throw new UnsupportedOperationException();
         }

         public int size() {
            throw new UnsupportedOperationException();
         }
      };
      this.expectedCommentTypes = expectedCommentTypes;
      this.commentLineList = new ArrayList();
   }

   public CommentEventsCollector(Queue<Event> eventSource, CommentType... expectedCommentTypes) {
      this.eventSource = eventSource;
      this.expectedCommentTypes = expectedCommentTypes;
      this.commentLineList = new ArrayList();
   }

   private boolean isEventExpected(Event event) {
      if (event != null && event.is(Event.ID.Comment)) {
         CommentEvent commentEvent = (CommentEvent)event;
         CommentType[] types = this.expectedCommentTypes;
         int typeCount = types.length;

         for (int i = 0; i < typeCount; ++i) {
            CommentType type = types[i];
            if (commentEvent.getCommentType() == type) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public CommentEventsCollector collectEvents() {
      this.collectEvents((Event)null);
      return this;
   }

   public Event collectEvents(Event event) {
      if (event != null) {
         if (!this.isEventExpected(event)) {
            return event;
         }

         this.commentLineList.add(new CommentLine((CommentEvent)event));
      }

      while(this.isEventExpected((Event)this.eventSource.peek())) {
         this.commentLineList.add(new CommentLine((CommentEvent)this.eventSource.poll()));
      }

      return null;
   }

   public Event collectEventsAndPoll(Event event) {
      Event nextEvent = this.collectEvents(event);
      return nextEvent != null ? nextEvent : (Event)this.eventSource.poll();
   }

   public List<CommentLine> consume() {
      List<CommentLine> result;
      try {
         result = this.commentLineList;
      } finally {
         this.commentLineList = new ArrayList();
      }

      return result;
   }

   public boolean isEmpty() {
      return this.commentLineList.isEmpty();
   }
}
