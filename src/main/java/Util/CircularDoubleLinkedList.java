/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

/**
 *
 * @author ronald cabrera palacios
 * @param <E>
 */

public class CircularDoubleLinkedList<E> implements List<E> {
    private Node<E> ultimo;
    private int current;

    public CircularDoubleLinkedList() {
        ultimo = null;
        current = 0;
    }
    
    private class Node<E>{
        private E data;
        private Node<E> next;
        private Node<E> previous;
        
        public Node(E data) {
            this.data = data;
            this.next = null;
            this.previous = null;
        }
    }
       
    @Override
    public boolean addFirst(E e) {
        if(e == null) 
            return false;
        
        Node<E> n = new Node(e);
        
        if(isEmpty()) {
            ultimo = n; 
            ultimo.next = n;
        }
        else{
            Node<E> siguiente = ultimo.next;
            ultimo.next = n;
            n.next = siguiente;
            siguiente.previous = n;
            n.previous = ultimo;
        }
        current ++;
        return true;
    }

    @Override
    public boolean addLast(E e) {
        if(e == null) 
            return false;
        
        Node<E> n = new Node(e);
        
        if(isEmpty()) {
            ultimo = n;
            ultimo.next = n;
        }
        else{
            n.previous = ultimo;
            Node<E> primero = ultimo.next;
            n.next = primero;
            primero.previous = n;
            ultimo.next = n;
            ultimo = n;
        }
        current++;
        return true;
    }

    @Override
    public E getFirst() {
        if(isEmpty()){
            throw new IllegalStateException("La lista esta vacia");
        }
        return ultimo.next.data;
    }

    @Override
    public E getLast() {
        if(isEmpty()){
            throw new IllegalStateException("La lista esta vacia");
        }
        return ultimo.data;
    }

    @Override
    public int indexOf(E e) {
        if(e == null){ 
            return -1;
        }    
        if(isEmpty()){
            return -1;
        }
        
        Node<E> n = ultimo.next;
        
        for(int i = 0; i<current; i++){
            if(n.data.equals(e)){
                return i;
            }
            n = n.next;
        }
        return -1;
    }

    @Override
    public int size() {
        return current;
    }

    @Override
    public boolean removeLast() {
        if(isEmpty()){
            return false;
        }
        
        if(current == 1){
            ultimo.data = null;
            ultimo.next = null;
            ultimo.previous = null;
            ultimo = null;
        }
        else{
            Node<E> n = ultimo.previous;
            Node<E> primero = ultimo.next;
            n.next = primero;
            primero.previous = n;
            ultimo.data = null;
            ultimo.next = null;
            ultimo.previous = null;
            ultimo = n;
        }
        current--;
        return true;
    }

    @Override
    public boolean removeFirst() {
        if(isEmpty()){
            return false;
        }
        
        if(current == 1){
            ultimo.data = null;
            ultimo.next = null;
            ultimo.previous = null;
            ultimo = null;
        }
        else{
            Node<E> primero = ultimo.next;
            Node<E> segundo = primero.next;
            ultimo.next = segundo;
            segundo.previous = ultimo;
            primero.data = null;
            primero.next = null;
            primero.previous = null;    
        }
        current--;
        return true;
    }
    
    public Node<E> obtenerNodo(int index){ //en este no se verifica ya que se usa dentro de las funciones con verificacion
        if(index == 0){
            return ultimo.next;
        }
        if(index == current-1){
            return ultimo;
        }
        Node<E> temporal = ultimo.next;
        for(int i=0; i<index;i++){
            temporal = temporal.next;
        }
        return temporal;
    }
     
    @Override
    public boolean insert(int index, E e) {
        if(e == null){
            return false;
        }
        if(index > current - 1 || index<0){
            throw new IndexOutOfBoundsException("El indice supera el tamanio de la lista.");
        }
       
        if(index == 0){
            return addFirst(e);
        }
        
        if(index == current-1){
            return addLast(e);
        }
        
        Node<E> n = new Node<>(e);
        Node<E> m = obtenerNodo(index);
        Node<E> anterior = m.previous;
        anterior.next = n;
        n.previous = anterior;
        n.next = m;
        m.previous = n;
        current++;
        return true;
    }

    @Override
    public boolean set(int index, E e) {
        if(index > current-1 || index<0){
            throw new IndexOutOfBoundsException("El indice supera el tamanio de la lista.");
        }
        if(e == null){
            return false;
        }
        if(index == current-1){
            ultimo.data = e;
            return true;
        }
        if(index == 0){
            ultimo.next.data = e;
            return true;
        }
        Node<E> n = obtenerNodo(index);
        n.data = e;
        return true;
    }

    @Override
    public boolean isEmpty() {
        return current == 0;
    }

    @Override
    public E get(int index) {
        if(index > current-1 || index<0){
            throw new IndexOutOfBoundsException("El indice supera el tamanio de la lista.");
        }
        if(index == 0){
            return ultimo.next.data;
        }
        if(index == current-1){
            return ultimo.data;
        }

        Node<E> n = obtenerNodo(index);
        return n.data;
    }

    @Override
    public boolean contains(E e) {
        if(e == null){
            return false;
        }
        for(int i=0; i<current-1;i++){
            Node<E> n = obtenerNodo(i);
            if(n.data.equals(e)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(int index) {
        if(index > current-1 || index < 0){
            throw new IndexOutOfBoundsException("El indice supera el tamanio de la lista.");
        }
        if(index == 0){
            return removeFirst();
        }
        if(index == current-1){
            return removeLast();
        }
        else{
            Node<E> anterior = obtenerNodo(index-1);
            Node<E> nodoElim = anterior.next;
            Node<E> siguiente = nodoElim.next;
            //apuntar el anterior con el siguiente 

            anterior.next = siguiente;
            siguiente.previous = anterior;
            nodoElim.data = null;
            nodoElim.next = null;
            nodoElim.previous = null;
            current--;
            return true;
        }
    }


}
