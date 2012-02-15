package risicammara2.client;

import javax.swing.JButton;

/**
 *
 * @author stengun
 */
public class JButton_player extends JButton{
    private long player_id;

    public JButton_player() {
        this.player_id = -1;
    }

    public long getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(long player_id) {
        this.player_id = player_id;
    }
    
    
}
