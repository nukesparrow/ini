/*
 * Copyright 2015 Nuke Sparrow <nukesparrow@bitmessage.ch>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nukesparrow.collections;

import java.util.AbstractList;
import java.util.Collection;

/**
 *
 * @author Nuke Sparrow <nukesparrow@bitmessage.ch>
 */
public class IntegerArrayList extends AbstractList<Integer> {

    public IntegerArrayList() {
        this(16);
    }

    public IntegerArrayList(int initialCapacity) {
        array = new int[initialCapacity];
    }

    private int[] array;
    private int size = 0;

    private void growIfNecessary() {
        if (size == array.length) {
            int[] n = new int[size * 2];
            System.arraycopy(array, 0, n, 0, size);
            array = n;
        }
    }

    private void growIfNecessary(int requiredCapacity) {
        if ((array.length - size) < requiredCapacity) {
            int[] n = new int[Math.max(requiredCapacity + size, array.length * 2)];
            System.arraycopy(array, 0, n, 0, size);
            array = n;
        }
    }

    public boolean addAll(int[] arr, int offset, int length) {
        growIfNecessary(length);
        
        System.arraycopy(arr, offset, array, size, length);
        size += arr.length;
        
        return true;
    }

    public boolean addAll(int[] arr) {
        return addAll(arr, 0, arr.length);
    }

    public boolean add(int e) {
        growIfNecessary();
        
        array[size++] = e;
        
        return true;
    }

    @Override
    public boolean add(Integer e) {
        return add(e.intValue());
    }

    public void add(int index, int element) {
        if (index == size) {
            add(element);
        } else {
            growIfNecessary();
            for(int i = index; i < size; i++)
                array[i + 1] = array[i];
            size++;
            array[index] = element;
        }
    }

    @Override
    public void add(int index, Integer element) {
        add(index, element.intValue());
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public Integer get(int index) {
        return array[index];
    }

    public int getInt(int index) {
        return array[index];
    }

    @Override
    public Integer remove(int index) {
        int v = array[index];
        size--;
        for(int i = size; i >= index; i--)
            array[i] = array[i + 1];
        return v;
    }

    @Override
    public Integer set(int index, Integer element) {
        return set(index, element.intValue());
    }

    public int set(int index, int element) {
        int s = array[index];
        array[index] = element;
        return s;
    }

    @Override
    public int size() {
        return size;
    }

    public int[] toIntegerArray() {
        int[] r = new int[size];
        System.arraycopy(array, 0, r, 0, size);
        return r;
    }

}
