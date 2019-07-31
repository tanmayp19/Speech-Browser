import java.io.*;
class InputFileFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File fileobj){
        String extension="";
        if(fileobj.getPath().lastIndexOf('.')>0)
            extension=fileobj.getPath().substring(fileobj.getPath().lastIndexOf('.')+1).toLowerCase();
        if(extension!="")
            return (extension.equals("html"));
        else
            return fileobj.isDirectory() ;
    }
    public String getDescription() {
        return "HTML or word Files(*.html)";
    }
}
