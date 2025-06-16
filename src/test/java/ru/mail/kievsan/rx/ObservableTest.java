package ru.mail.kievsan.rx;

import ru.mail.kievsan.rx.core.Observable;
import ru.mail.kievsan.rx.schedulers.IOScheduler;
import ru.mail.kievsan.rx.schedulers.SingleScheduler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


class ObservableTest {

    @Test
    void testCreateAndSubscribe() {
        List<String> received = new ArrayList<>();
        Observable<String> source = Observable.create(observer -> {
            observer.onNext("a");
            observer.onNext("b");
            observer.onComplete();
        });

        source.subscribe(received::add,
                Throwable::printStackTrace,
                () -> received.add("complete"));

        assertEquals(List.of("a", "b", "complete"), received);
    }

    @Test
    void testJustSingleElement() {
        List<Integer> list = new ArrayList<>();
        Observable.just(0).subscribe(list::add);

        assertEquals(1, list.size());
        assertEquals(0, list.getFirst());
    }

    @Test
    void testSubscribeOnScheduler() throws InterruptedException {
        AtomicReference<String> threadName = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Observable.just("data")
                .subscribeOn(new IOScheduler())
                .subscribe(item -> {
                    threadName.set(Thread.currentThread().getName());
                    latch.countDown();
                });

        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertNotEquals(Thread.currentThread().getName(), threadName.get());
    }

    @Test
    void testObserveOnScheduler() throws InterruptedException {
        AtomicReference<String> threadName = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Observable.just("data")
                .observeOn(new SingleScheduler())
                .subscribe(item -> {
                    threadName.set(Thread.currentThread().getName());
                    latch.countDown();
                });

        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertTrue(threadName.get().startsWith("pool-"));
    }
}

