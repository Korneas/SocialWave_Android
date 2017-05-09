package serial;

import java.io.Serializable;

public class Post implements Serializable {

    private String autor, msg, name;
    private int tipo, id;
    private byte[] file;
    private String[] likes;

    public Post(String autor, String msg, String name, int tipo, int id, byte[] file, String[] likes) {
        this.autor = autor;
        this.msg = msg;
        this.name = name;
        this.tipo = tipo;
        this.id = id;
        this.file = file;
        this.likes = likes;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String[] getLikes() {
        return likes;
    }

    public void setLikes(String[] likes) {
        this.likes = likes;
    }
}