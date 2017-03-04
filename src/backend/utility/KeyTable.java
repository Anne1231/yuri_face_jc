package backend.utility;

import java.util.HashSet;

/**
 * Created by Akihiro on 2017/03/04.
 */
public class KeyTable {
    private HashSet<Character> key_hashset;

    public KeyTable(){
        key_hashset = new HashSet<>();
    }

    public void press(Character character){
        key_hashset.add(character);
    }

    public void release(Character character){
        key_hashset.remove(character);
    }

    private boolean isPressed(Character character){
        return key_hashset.contains(character);
    }
}
