/**
 * @author A0000000X
 */

package cs2030s.fp;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public abstract class Reader<T> {

  private static final Empty empty = new Empty();

  private static final class Empty extends Reader<Object> {
    @Override
    public Object read() throws NoSuchElementException {
      throw new NoSuchElementException();
    }

    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public Reader<Object> consume() throws NoSuchElementException{
      throw new NoSuchElementException();
    }

    @Override
    public <U> Reader<U> map(Immutator<? extends U, ? super Object> transformer) {
      return Reader.of();
    }

    @Override
    public LinkedList<Object> get() {
      return new LinkedList<>();
    }

    @Override
    public Reader<Object> flatMap(Immutator<? extends Reader<? extends Object>, ? super Object> transformer) {
      return Reader.of();
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof Empty ? true : false;
    }

    @Override
    public String toString() {
      return "EOF";
    }
  }

  private static final class Present<U> extends Reader<U> {
    private LinkedList<U> content;

    private Present(LinkedList<U> content) {
      this.content = content;
    }
    
    @Override
    public U read() throws NoSuchElementException {
      return content.peek();
    }

    @Override
    public boolean hasNext() {
      return true;
    }

    @Override
    public Reader<U> consume() {
      LinkedList<U> copy = new LinkedList<>();
      for (int i = 1; i < this.content.size(); i++) {
        copy.add(this.content.get(i));
      }
      if (copy.isEmpty()) return Reader.of();
      else return new Present<>(copy);
    }

    @Override
    public <V> Reader<V> map(Immutator<? extends V, ? super U> transformer) {
      LinkedList<V> transformed = new LinkedList<>();
      for (int i = 0; i < this.content.size(); i++) {
        transformed.add(transformer.invoke(this.content.get(i)));
      }
      return new Present<>(transformed);
    }

    @Override
    public LinkedList<U> get() {
      return this.content;
    }

    @Override
    public Reader<U> flatMap(Immutator<? extends Reader<? extends U>, ? super U> transformer) {
      LinkedList<U> result = new LinkedList<>();
      Reader<? extends U> list = transformer.invoke(this.read());
      result.addAll(list.get());
      result.addAll(this.consume().get());
      return new Present<U>(result);
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof Present ? true : false;
    }

    @Override
    public String toString() {
      return "Reader";
    }
  }

  public static <U> Reader<U> of() {
    @SuppressWarnings("unchecked")
    Reader<U> empty = (Reader<U>) Reader.empty;
    return empty;
  }

  @SafeVarargs
  public static <U> Reader<U> of(U ... inputs) {
    LinkedList<U> arr = new LinkedList<>();
    for (U i : inputs) {
      arr.add(i);
    }
    return new Present<>(arr);
  }

  public abstract T read() throws NoSuchElementException;

  public abstract boolean hasNext();
  
  public abstract Reader<T> consume();

  public abstract <U> Reader<U> map(Immutator<? extends U, ? super T> transformer);

  public abstract Reader<T> flatMap(Immutator<? extends Reader<? extends T>, ? super T> transformer);

  public abstract LinkedList<T> get();
}
