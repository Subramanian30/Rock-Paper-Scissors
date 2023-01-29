import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class game {

    public static String player1choice(Random random, String name){

        int wordNumber;

        wordNumber = random.nextInt( 3 ) + 1;
        String user1Choice = "";


        if( wordNumber == 1 )
        {
            user1Choice = "rock";
        }else if( wordNumber == 2 )
        {
            user1Choice = "paper";
        }
        else if( wordNumber == 3 ){
            user1Choice = "scissors";
        }

        System.out.println(name+ " has already made his choice" );
        return user1Choice;

    }

    public static String player2choice( Scanner scanner ){

        String user2Choice;
        System.out.println( "Options to choose from :\n1.Rock\n2.Paper\n3.Scissors" );
        System.out.print( "\nPlease make yours : " );
        user2Choice = scanner.nextLine();

        return user2Choice;

    }

    public static String chooseWinner( String user1choice, String user2Choice,String Player1,String Player2){

        String winner = "No Winner";
        String customMessage = "Both choose same";

        String rockCustomMessage = "The rock smashes the scissor";
        String scissorsCustomMessage = "Scissors cuts paper";
        String paperCustomMessage = "Paper wraps rock";
        //Rock to win
        if( user1choice.equals( "rock" ) && user2Choice.equalsIgnoreCase( "scissors" ) )
        {
            winner = Player1;
            customMessage = rockCustomMessage;
        }

        else if( user2Choice.equalsIgnoreCase( "rock" ) && user1choice.equals( "scissors" ) )
        {
            winner = Player2;
            customMessage = rockCustomMessage;
        }

        //Scissors to win

        if( user1choice.equals( "scissors" ) && user2Choice.equalsIgnoreCase( "paper" ) )
        {
            winner = Player1;
            customMessage = scissorsCustomMessage;
        }

        else if( user2Choice.equalsIgnoreCase( "scissors" ) && user1choice.equals( "paper" ) )
        {
            winner = Player2;
            customMessage = scissorsCustomMessage;
        }

        //Paper to win

        if( user1choice.equals( "paper" ) && user2Choice.equalsIgnoreCase( "rock" ) ){
            winner = Player1;
            customMessage = paperCustomMessage;
        }

        else if( user2Choice.equalsIgnoreCase( "paper" ) && user1choice.equals( "rock" ) ){
            winner = Player2;
            customMessage = paperCustomMessage;
        }

        System.out.println(customMessage);
        System.out.println("Winner of this round is "+winner);

        return winner;

    }

    public static void insert(String name, int score) throws Exception {

        Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/rock","root","subbu123");
        String insert="insert into score(name,score) values(?,?)";

        PreparedStatement st= connection.prepareStatement(insert);


        st.setString(1,name);
        st.setString(2, String.valueOf(score));

        st.executeUpdate();
    }

    public static void max()
    {
        Connection con;
        Statement statement;
        try {

            HashMap <String,Integer> hm = new HashMap<>();

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/rock","root","subbu123");
            statement = con.createStatement();

            String sql;
            sql = "select name, score from score order by score desc";

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                    hm.put(resultSet.getString("name"), resultSet.getInt("score"));
                    break;
                }

            hm.forEach((k, v)
                    -> System.out.println(k + " : " + (v)));

        } catch (Exception e) {

        }
    }


    public static void main(String[] args) throws Exception {
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        Scanner scanner = new Scanner(System.in);
        Scanner sc= new Scanner(System.in);

        Random random = new Random();

        String user1choice;
        String user2choice;

        String winner;

        int score2=0;
        int score1=0;

        String player1name;
        String player2name;


        int stop=1;


        System.out.println("Player With Max Points Scored: ");
        max();

        System.out.println("Game To end After 10 Rounds of play");

        System.out.println("Enter Name Of Player 1");
        player1name = br.readLine();


        System.out.println("Enter Name Of Player 2");
        player2name = br.readLine();


        while(true){


            System.out.println("Round "+ stop);
            user1choice = player1choice(random, player1name);


            user2choice= player2choice(scanner);
            if(user2choice.equalsIgnoreCase("paper") || user2choice.equalsIgnoreCase("rock") || user2choice.equalsIgnoreCase("scissors")) {
                winner = chooseWinner(user1choice, user2choice, player1name, player2name);


                if (winner.equals(player2name))
                    score2 += 5;

                else if (winner.equals(player1name))
                    score1 += 5;
            }
            else {
                System.out.println("Wrong Input by "+player2name+" so score awarded to "+player1name);
                System.out.println("Winner of this round is "+player1name);
                score1 += 5;
            }

            System.out.println("After Round "+stop+" scores are");
            System.out.println(player1name+" : "+score1);
            System.out.println(player2name+" : "+score2);


            stop++;


            System.out.println("Options: 1.Continue 2.Exit");
            int choice= sc.nextInt();
            if (stop>10 || choice==2) {
                System.out.println("Thank You Have finished the game");
                break;
            }
        }


        if(score1==score2)
            System.out.println("Draw Game");
        else if(score1<score2)
            System.out.println(player2name+" wins the game");
        else
            System.out.println(player1name+" wins the game");



        //Insert into database
        insert(player1name,score1);
        insert(player2name,score2);


    }
}
