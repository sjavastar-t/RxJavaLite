package ru.mail.kievsan.rx.core;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Групповая отмена нескольких подписок
 */
public class CompositeDisposable {
    private final Set<Disposable> disposables = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Добавляет Disposable в группу
     *
     * @param d Disposable к добавлению
     */
    public void add(Disposable d) {
        disposables.add(d);
    }

    /**
     * Удаляет Disposable из группы
     *
     * @param d Disposable к удалению
     */
    public void remove(Disposable d) {
        disposables.remove(d);
    }

    /**
     * Отменяет все подписки в группе
     */
    public void dispose() {
        for (Disposable d : disposables) {
            d.dispose();
        }
        disposables.clear();
    }

    /**
     * Проверяет отмену подписок
     *
     * @return true, если все подписки отменены
     */
    public boolean isDisposed() {
        return disposables.stream().allMatch(Disposable::isDisposed);
    }
}

