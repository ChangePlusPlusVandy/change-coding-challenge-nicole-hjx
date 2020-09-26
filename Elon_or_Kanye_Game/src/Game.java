import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends JFrame implements ActionListener {
    // stores correct guesses count and wrong guesses count
    private int right;
    private int wrong;
    // stores guesses made
    private int count = 0;

    private List<Twitter> twitters;
    // the tweet the user is guessing now
    private Twitter current;

    // show tweet's content
    private JTextArea twitterText;

    private JLabel guessLabel;

    // guess kanye
    private JButton kanyeButton;
    // guess elon
    private JButton elonmuskButton;

    // show correct and wrong label
    private JLabel resultLabel;

    private void init() {
        right = 0;
        wrong = 0;
        Rule rule = new Rule();
        List<Twitter> kanyewestTwitters = rule.getTwitters(Twitter.KANYEWEST_ID);
        List<Twitter> elonmuskTwitters = rule.getTwitters(Twitter.ELONMUSK_ID);
        twitters = new ArrayList<>();
        twitters.addAll(kanyewestTwitters);
        twitters.addAll(elonmuskTwitters);

        twitterText = new JTextArea();
        twitterText.setLineWrap(true);
        guessLabel = new JLabel("GUESS: ");
        kanyeButton = new JButton("Kanye West");
        elonmuskButton = new JButton("Elon Musk");
        kanyeButton.addActionListener(this);
        elonmuskButton.addActionListener(this);

        resultLabel = new JLabel("right: " + right + ", wrong: " + wrong);


        Box b1 = Box.createVerticalBox();
        b1.add(Box.createHorizontalStrut(200));
        b1.add(twitterText);

        Box b2 = Box.createHorizontalBox();
        b2.add(guessLabel);
        b2.add(kanyeButton);
        b2.add(elonmuskButton);
        b1.add(b2);

        b1.add(resultLabel);
        add(b1);

        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // guess next tweet
    private void start() {
        getRandomTwitter();
        twitterText.setText(current.getText());
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // according to the button being clicked, decide which person the user chose
        long userId = 0;
        if (e.getSource() == kanyeButton) {
            userId = Twitter.KANYEWEST_ID;
        } else if (e.getSource() == elonmuskButton) {
            userId = Twitter.ELONMUSK_ID;
        }
        count++;
        // check if user is correct
        String message = "";
        if (isRight(userId)) {
            right++;
            message = "You are right! ";
        } else {
            wrong++;
            message = "You are wrong! ";
        }

        // update the results
        resultLabel.setText("right: " + right + ", wrong: " + wrong);

        // show the result of this guess, ask if user wanna continue
        int result = JOptionPane.showConfirmDialog(this, message + "Continue?", "Result", JOptionPane.YES_NO_OPTION);
        // user chooses to continue
        if (result == 0) {
            start();
        }
        // user chooses not to continue
        else {
            // calculate the accuracy
            float accuracy = (float) right / (right + wrong) * 100;
            // show accuracy
            JOptionPane.showMessageDialog(this, "Accuracy: " + accuracy + "%\n" +
                    "Guesses made: " + count);
            System.exit(0);
        }
    }


    // get random tweet and delete it so it will not be chosen again
    private void getRandomTwitter() {
        int random = new Random().nextInt(twitters.size() + 1);
        current = twitters.remove(random);
    }
    
    private boolean isRight(long userId) {
        if (current != null) {
            return userId == current.getUserId();
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.init();
        game.start();
    }
}
