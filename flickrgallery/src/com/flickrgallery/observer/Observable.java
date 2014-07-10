package com.flickrgallery.observer;

public interface Observable {
	public void registerObserver(Observer observer);
	public void notifyObservers();
}
