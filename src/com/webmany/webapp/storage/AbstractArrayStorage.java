package com.webmany.webapp.storage;

import com.webmany.webapp.exception.StorageException;
import com.webmany.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;


public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected final static int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    protected abstract void fillDeletedElement(int index);

    protected abstract void insertElement(Resume resume, int index);


    @Override
    public void clear() {      // удалям все данные из хранилища
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected void doUpdate(Resume resume, Integer searchKey) {
        storage[searchKey] = resume;
    }

    @Override
    protected void doSave(Resume resume, Integer searchKey) {
      if (size == STORAGE_LIMIT) {
          throw new StorageException("Storage Overflow", resume.getUuid());
      } else {
        insertElement(resume, searchKey);
        size++;
      }
    }

    @Override
    public void doDelete(Integer index) {
        fillDeletedElement(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected Resume doGet(Integer index) {
        return storage[index];
    }

    @Override
    public List<Resume> doCopyAll() {
        return Arrays.asList(Arrays.copyOf(storage, size));  // возвращаем копию storage
    }

    public int size() {       // возвращаем размер массива
        return size;
    }

    @Override
    protected boolean isExist(Integer index) {
        return index >= 0;
    }



}