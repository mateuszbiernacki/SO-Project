package FileSystem;

import java.util.Vector;

public class FileManager extends  Files{

    public static Vector<Files> mainCatalog = new Vector<Files>();
    public static Vector<Character> openedFiles = new Vector<Character>();

    public static int readAddress(char index){
        int address = -1;
        if(index == '0')address = 0;
        if(index == '1')address = 1;
        if(index == '2')address = 2;
        if(index == '3')address = 3;
        if(index == '4')address = 4;
        if(index == '5')address = 5;
        if(index == '6')address = 6;
        if(index == '7')address = 7;
        if(index == '8')address = 8;
        if(index == '9')address = 9;
        if(index == 'a')address = 10;
        if(index == 'b')address = 11;
        if(index == 'c')address = 12;
        if(index == 'd')address = 13;
        if(index == 'e')address = 14;
        if(index == 'f')address = 15;
        if(index == 'g')address = 16;
        if(index == 'h')address = 17;
        if(index == 'i')address = 18;
        if(index == 'j')address = 19;
        if(index == 'k')address = 20;
        if(index == 'l')address = 21;
        if(index == 'm')address = 22;
        if(index == 'n')address = 23;
        if(index == 'o')address = 24;
        if(index == 'p')address = 25;
        if(index == 'q')address = 26;
        if(index == 'r')address = 27;
        if(index == 's')address = 28;
        if(index == 't')address = 29;
        if(index == 'u')address = 30;
        if(index == 'v')address = 31;
        if(index == 'w')address = 32;
        return address;
    }
    public static char writeAddress(int address){
        char index = '-';
        if(address == 0)index = '0';
        if(address == 1)index = '1';
        if(address == 2)index = '2';
        if(address == 3)index = '3';
        if(address == 4)index = '4';
        if(address == 5)index = '5';
        if(address == 6)index = '6';
        if(address == 7)index = '7';
        if(address == 8)index = '8';
        if(address == 9)index = '9';
        if(address == 10)index = 'a';
        if(address == 11)index = 'b';
        if(address == 12)index = 'c';
        if(address == 13)index = 'd';
        if(address == 14)index = 'e';
        if(address == 15)index = 'f';
        if(address == 16)index = 'g';
        if(address == 17)index = 'h';
        if(address == 18)index = 'i';
        if(address == 19)index = 'j';
        if(address == 20)index = 'k';
        if(address == 21)index = 'l';
        if(address == 22)index = 'm';
        if(address == 23)index = 'n';
        if(address == 24)index = 'o';
        if(address == 25)index = 'p';
        if(address == 26)index = 'q';
        if(address == 27)index = 'r';
        if(address == 28)index = 's';
        if(address == 29)index = 't';
        if(address == 30)index = 'u';
        if(address == 31)index = 'v';
        if(address == 32)index = 'w';
        return index;
    }

    public static int isOpened(String name){
        /*
        * code == 0 plik otwarty
        * code == 1 plik nie zotal otwarty
        * code == 2 plik nie istnieje
         */
        int code = 2;
        for(int i = 0; i < mainCatalog.size(); i++) {
            if (name.equals(mainCatalog.get(i).fileName)){
                code = 1;
                for(int j = 0; j < openedFiles.size(); j++){
                    if (mainCatalog.get(i).indexBlock == openedFiles.get(j)) {
                        code = 0;
                        break;
                    }
                }
            }
        }
        return code;
    }
    public static boolean createFile(String name){
        Files file = new Files();
        int i = 0;
        boolean created = false;
        while(created == false && i < 256){
            if(bitMap[i] == 0){
                for(int j = 0; j < blockSize; j++){
                    disk[i][j] = '-';
                }
                created = true;
                file.indexBlock = writeAddress(i);
                file.fileName = name;
                file.Size = 8;
                bitMap[i] = 1;
            }
            i++;
        }
        mainCatalog.add(file);
        return created;
    }
    public static boolean openFile(String name){
        /*
        * opened == false plik nieistnieje
        * opened == true pomyslnie otwarto plik
         */
        boolean opened = false;
        for(int i = 0; i < mainCatalog.size(); i++){
            if(name == mainCatalog.get(i).fileName) {
                openedFiles.add(mainCatalog.get(i).indexBlock);
                opened = true;
                break;
            }
        }
        return opened;
    }
    public static int writeFile(String name, String data){
        /*
        * Zmienna code oznacza status errorow
        * 1 - wszystko git
        * 2 - brak wolnych blokow do zapisu.
        * 3 - Plik osiagnal maksymalna wielkosc 64 bajtow plik za duzy. Przekroczyl 64 znaki == 64 bajty
        * 4 - Nie znaleziono pliku w wektorze otwartych plikow (czyt. plik nie zostal otwarty)
        * */
        int pointer = 0;
        int code = 0;
        char index = '-';
        int blocksAmount = 0;

        for(int i = 0; i < mainCatalog.size(); i++){
            if(name.equals(mainCatalog.get(i).fileName)){
                for(int j = 0; j < openedFiles.size(); j++){
                    if(mainCatalog.get(i).indexBlock == openedFiles.get(j)){
                        index = openedFiles.get(j);
                        code = 1;
                    }
                }
            }
        }

        if(code == 1){
            for(int i = 0; i < blockSize; i++){
                if(disk[index][i] == '-'){
                    blocksAmount++;
                }
            }
            if(blocksAmount == 0){
                code = 2;
            }
            else if(blocksAmount * 16 < data.length()){
                code = 3;
            }
            else{
                for(int i = 0; i < bitMap.length; i++){
                    if(bitMap[i] == 0){
                        for(int j = 0; j < blockSize; j++){
                            disk[i][j] = data.charAt(pointer + j);
                            pointer++;
                        }
                        bitMap[i] = 1;
                        for(int g = 0; g < blockSize; g++){
                            if(disk[index][g] == '-'){
                                disk[index][g] = writeAddress(i);
                            }
                        }
                    }
                }
            }
        }
        return code;
    }
    //TODO
    public static void readFile(String name, int howMuch, short ramAddr){
        /*
        * code = 1 wszystko git
        *
        *
        *
         */
        int code = 0;
        char index = '-';

        for(int i = 0; i < mainCatalog.size(); i++){
            if(name.equals(mainCatalog.get(i).fileName)){
                for(int j = 0; j < openedFiles.size(); j++){
                    if(mainCatalog.get(i).indexBlock == openedFiles.get(j)){
                        index = openedFiles.get(j);
                        code = 1;
                    }
                }
            }
        }

        for(int i = 0; i < blockSize; i++){
            if(disk[index][i] != '-'){

            }
        }


    }
    //TODO
    public static int closeFile(String name){
        int code = -1;

        return code;
    }

    public static int deleteFile(String name){
        /*
        * code == 1 plik jest otwarty nie mozna usunac otwartego pliku
        * code == 2 nie ma takiego pliku
        * code == 3 usunieto pomyslnie
         */
        char index;
        char toErase = '-';
        int code = -1;
        if(isOpened(name) == 0){
            code = 1;
        }
        else{
            for(int i = 0; i < mainCatalog.size(); i++){
                code = 2;
                if(name.equals(mainCatalog.get(i).fileName)){
                    index = mainCatalog.get(i).indexBlock;
                    for(int j = 0; j < blockSize; j++) {
                        if (disk[index][j] != '-') {
                            disk[index][j] = toErase;
                            for (int g = 0; g < blockSize; g++) {
                                disk[toErase][g] = '-';
                            }
                            disk[index][j] = '-';
                        }
                    }
                    code = 3;
                    break;
                }
            }
        }
        return code;
    }
}
