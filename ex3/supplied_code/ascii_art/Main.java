package ascii_art;

public class Main {
    public static void main(String[] args) {
        char[] charset = {'m', 'o'};
        String path = "ex3/examples/board.jpeg";
        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(path, charset, 2);
        char[][] asciiArt = asciiArtAlgorithm.run();
        System.out.println(asciiArt);
    }
}
