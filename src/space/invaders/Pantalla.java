package space.invaders;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Esta clase hereda métodos y atributos de la clase JFrame, contiene métodos y
 * atributos de la clase JFrame e implementa una clase llamada KeyListener.
 * Agrupa en su totalidad todo tipo de variables y métodos de un juego en
 * desarrollo.
 *
 * @version 1.2
 * @author Andre
 */
public final class Pantalla extends JFrame implements KeyListener {

    JPanel panel;

    int h = 700; // altura de la pantalla
    int b = 500; // base de la pantalla

    int h_ship = 96; // altura
    int b_ship = 90; // base
    int v_ship = 35; // velocidad
    int x_ship = (b / 2) - (b_ship / 2); // posición en x
    int y_ship = h - h_ship - 50; // posición en y
    int x_shipL = x_ship - b; // posición de la nave impostora izquierda
    int x_shipR = x_ship + b; // posición de la nave impostora derecha
    int[] x_bs = new int[10000]; // posición de proyectiles en x
    int[] y_bs = new int[x_bs.length]; // posición de proyectiles en y
    int v_bs = 25; // velocidad de proyectiles
    int c_bs = 0; // número de proyectil actual

    Random azar = new Random(); // objeto de tipo aleatorio

    int b_storm = 24; // base del bonus
    int h_storm = 32; // altura del bonus
    int x_storm = azar.nextInt(b - b_storm); // posición aleatoria inicial del bonus en x
    int y_storm = -1000; // posición inicial del bonus en y

    int rows = 2; // número de filas
    int columns = 3; // número de columnas
    int n = columns * rows; // número total

    int h_invader = 51; // altura
    int b_invader = 70; // base
    int vx_invader = 10; // velocidad en x
    int vy_invader = 30; // velocidad en y
    int[] x_invader = new int[n + 1]; // posición en x
    int[] y_invader = new int[n + 1]; // posición en y
    int[] x_invaderF = new int[n + 1]; // posición en x de invasor falso
    int[] y_invaderF = new int[n + 1]; // posición en y de invasor falso
    int[] x_bi = new int[10000]; // posición de proyectiles en x
    int[] y_bi = new int[x_bi.length]; // posición del proyectiles en y
    int v_bi = 15; // velocidad de proyectiles
    int c_bi = 0; // número actual del proyectil
    int counter = 0; // contador que se reinicia cuando iguala la frecuencia de los proyectiles
    int frequency = 30; // valor que determina la frecuencia de disparo; cuanto mayor sea, menor será la frecuencia
    boolean[] alive = new boolean[n]; // 

    int color = 0; // color para un "Switch" que asigna colores a los invasores
    int _invaders = 5; // espaciado entre invasores
    int yo_invaders = 50; // posición inicial en y del bloque de invasores
    int yo_invadersF = -((h_invader + _invaders) * rows); // posición inicial en y del bloque de invasores falsos
    int first_row; // posición de cada fila de invasores
    int first_rowF; // posición de cada fila de invasores falsos
    int re_invaders = yo_invaders; // posición de retorno de invasores en cada nivel
    int re_invadersF = yo_invadersF; // posición e retorno de invasores falsos en cada nivel
    int step; // paso de avance de invasores
    boolean restart = false; // booleano que establece cuando bajan los invasores
    int ti_fall = 0; // tiempo que duran los invasores en "bajar"
    int tf_fall = 15; // tiempo que deberían durar invasores en "bajar"
    int tf_level = 0; // tiempo para pasar de nivel
    int deaths = 0; // muertes de invasores

    int h_bullet = 15; // altura de proyectil
    boolean pause = true; // booleanos para pausar el juego entre niveles
    boolean shot = false; // booleano para detener el sonido de los disparos entre niveles
    double ti_game; // tiempo en que empieza el juego
    double tf_game; // tiempo en que termina el juego
    int lives = 3; // vidas del jugador
    int level = 1; // nivel actual
    int score = 0; // puntaje

    JLabel text_score = new JLabel("        Score: " + score + "        "); // Etiqueta con puntaje
    JLabel text_level = new JLabel("  LEVEL" + level + "  "); // Etiqueta con nivel
    JLabel text_death1 = new JLabel("   GAME   "); // Etiqueta con mensaje final
    JLabel text_death2 = new JLabel("   OVER   "); // Etiqueta con mensaje final

    /**
     * Constructor de la clase Pantalla; contiene métodos heredados de la clase
     * JFrame e implementa una interfaz (una clase con métodos abstractos)
     * llamada KeyListener.
     *
     * @version 1.2
     * @author Andre
     */
    public Pantalla() {
        super("Space Invaders");
        setIconImage(Toolkit.getDefaultToolkit().getImage("images/negro.png"));
        setVisible(true);
        setResizable(false);
        setSize(b, h);
        setLocationRelativeTo(null);
        inicio();
    }

    /**
     * Método para llamar a otro método que pinta objetos en la pantalla.
     */
    public void inicio() {
        addKeyListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Verficiación de la destrucción de los invasores
        ti_game = System.currentTimeMillis();
        revive_invaders();

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphic) {
                try {

                    if (lives == 0) {
                        text_death1.setVisible(true);
                        text_death2.setVisible(true);
                    }

                    double ti_level = System.currentTimeMillis();
                    text_score.setText("            Score: " + score + "            ");
                    text_level.setVisible(pause);

                    graphic.drawImage(charge_image("images/espacio.png"), 0, 0, b, h, null);
                    graphic.drawImage(charge_image("images/storm.png"), x_storm, y_storm, b_storm, h_storm, null);
                    y_storm += 2;
                    get_bonus();

                    if (lives != 0 & pause == false) {
                        shot = true;
                        double ti = System.currentTimeMillis();

                        first_row = yo_invaders;
                        first_rowF = yo_invadersF;

                        for (int i = 0; i < n; i++) {
                            x_invader[i + 1] = x_invader[i] + b_invader + _invaders;
                            y_invader[i] = first_row;

                            x_invaderF[i + 1] = x_invader[i + 1];
                            y_invaderF[i] = first_rowF;

                            if ((i + 1) % columns == 0) {
                                x_invader[i + 1] = x_invader[0];
                                first_row += h_invader + _invaders;

                                x_invaderF[i + 1] = x_invader[0];
                                first_rowF += h_invader + _invaders;
                            }

                            if (level % 2 == 0) {
                                color++;
                                if (color == 7) {
                                    color = 0;
                                }
                            }

                            // Graficación
                            if (alive[i] == true) {
                                BufferedImage current_color;
                                switch (color) {
                                    default:
                                    case 0:
                                    case 7:
                                        current_color = charge_image("images/verde.png");
                                        break;
                                    case 1:
                                    case 8:
                                        current_color = charge_image("images/azul.png");
                                        ;
                                        break;
                                    case 2:
                                        current_color = charge_image("images/morado.png");
                                        break;
                                    case 3:
                                        current_color = charge_image("images/rosado.png");
                                        break;
                                    case 4:
                                        current_color = charge_image("images/rojo.png");
                                        break;
                                    case 5:
                                        current_color = charge_image("images/naranja.png");
                                        break;
                                    case 6:
                                        current_color = charge_image("images/amarillo.png");
                                        break;
                                }
                                graphic.drawImage(current_color, x_invader[i], y_invader[i], b_invader, h_invader, null);
                                graphic.drawImage(current_color, x_invaderF[i], y_invaderF[i], b_invader, h_invader, null);
                            }

                            if (level % 2 != 0) {
                                if ((i + 1) % columns == 0) {
                                    color++;
                                }
                            }

                            if (i + 1 == n) {

                                if (level % 2 != 0) {
                                    color = color - rows;
                                }

                                if (x_invader[0] <= _invaders) {
                                    step = +(vx_invader);
                                } else if (x_invader[n - 1] >= b - (b_invader + _invaders)) {
                                    step = -(vx_invader);
                                    restart = true;
                                }

                                if (x_invader[0] <= _invaders & restart == true) {

                                    ti_fall = (int) (ti_fall + System.currentTimeMillis() - ti);

                                    if ((ti_fall) >= tf_fall) {
                                        yo_invaders += vy_invader;

                                        if (y_invader[i] >= h - h_invader) {
                                            yo_invadersF += vy_invader;
                                        }
                                        if (y_invader[0] >= h) {
                                            yo_invaders = yo_invadersF;
                                            yo_invadersF = re_invadersF;
                                        }

                                        ti_fall = 0;

                                        if (level % 2 != 0) {
                                            color++;
                                            if (color == 7) {
                                                color = 0;
                                            }
                                        }
                                    }
                                    x_invader[0] = 0;
                                    x_invaderF[0] = x_invader[0];
                                    restart = false;
                                } else {
                                    x_invader[0] = x_invader[0] + step;
                                    x_invaderF[0] = x_invader[0];
                                }

                            }
                        }

                        // ubicación de proyectiles de invasores
                        counter++;
                        if (counter == frequency) {
                            int shooter;
                            do {
                                shooter = azar.nextInt(n);
                            } while (alive[shooter] == false);
                            x_bi[c_bi] = x_invader[shooter] + b_invader / 2 - (h_bullet / 2) / 2;
                            y_bi[c_bi] = y_invader[shooter] + h_bullet;
                            c_bi++;
                            counter = 0;
                        }

                        // graficar proyectiles de invasores
                        for (int i = 0; i < c_bi; i++) {
                            graphic.setColor(Color.yellow);
                            graphic.fillRect(x_bi[i], y_bi[i], h_bullet / 2, h_bullet);
                            y_bi[i] += v_bi;
                        }

                        // Daño de proyectiles de invasores
                        for (int i = 0; i < c_bi; i++) {
                            if (y_bi[i] >= y_ship & y_bi[i] < y_ship + h_ship) {
                                if ((x_bi[i] + h_bullet / 4 >= x_ship & x_bi[i] < x_ship + b_ship) | (x_bi[i] + h_bullet / 4 >= x_shipL & x_bi[i] < x_shipL + b_ship) | (x_bi[i] + h_bullet / 4 >= x_shipR & x_bi[i] < x_shipR + b_ship)) {
                                    y_bi[i] = h + 100;
                                    lives--;
                                    if (lives != 0) {
                                        sound("images/damage.wav");
                                    } else if (lives == 0) {
                                        sound("images/explotion.wav");
                                    }
                                }
                            }
                        }

                        graph_ships(graphic, ImageIO.read(new File("images/nave.png")));

                        // Graficación de proyectiles nave
                        for (int i = 0; i < c_bs; i++) {
                            graphic.setColor(Color.cyan);
                            graphic.fillRect(x_bs[i], y_bs[i], h_bullet / 2, h_bullet);
                            y_bs[i] -= v_bs;
                            if (y_bs[i] <= 0) {
                                x_bs[i] = -200;
                            }
                        }

                        // Daño de proyectiles de nave
                        for (int i = 0; i < n; i++) {
                            for (int j = 0; j < c_bs; j++) {
                                if (alive[i] == true) {
                                    if ((y_bs[j] >= y_invader[i] & y_bs[j] <= y_invader[i] + h_invader) | (y_bs[j] >= y_invaderF[i] & y_bs[j] <= y_invaderF[i] + h_invader)) {
                                        if ((x_bs[j] + h_bullet / 4 >= x_invader[i] & x_bs[j] <= x_invader[i] + b_invader) | x_bs[j] + h_bullet / 4 >= x_invaderF[i] & x_bs[j] <= x_invaderF[i] + b_invader) {
                                            y_bs[j] = -100;
                                            alive[i] = false;
                                            deaths++;
                                            score += 10;
                                            sound("images/kill.wav");
                                        }
                                    }
                                }
                            }
                        }

                        // Representación de imágenes de vidas
                        lives(graphic, ImageIO.read(new File("images/mini.png")));

                        // Daño por contacto
                        for (int i = 0; i < n; i++) {
                            if (alive[i] == true) {
                                Rectangle real = new Rectangle(x_ship, y_ship, b_ship, h_ship);
                                Rectangle left = new Rectangle(x_shipL, y_ship, b_ship, h_ship);
                                Rectangle right = new Rectangle(x_shipR, y_ship, b_ship, h_ship);
                                Rectangle invasor = new Rectangle(x_invader[i], y_invader[i], b_invader, h_invader);
                                if (invasor.intersects(real) == true | invasor.intersects(left) == true | invasor.intersects(right) == true) {
                                    lives--;
                                    alive[i] = false;
                                    deaths++;
                                    score += 10;
                                    if (lives != 0) {
                                        sound("images/damage.wav");
                                    } else if (lives == 0) {
                                        sound("images/explotion.wav");
                                    }
                                }
                            }
                        }

                        if (lives == 0 & pause == false) {
                            x_storm = b;
                            y_storm = h;
                            tf_game = System.currentTimeMillis();
                            tf_game = tf_game - ti_game;
                            tf_game = tf_game / 1000;
                        }

                        if (lives != 0 & deaths == n) {
                            deaths = 0;
                            update_level();
                            pause = true;
                            shot = false;
                        }

                        repaint();

                    } else if (lives != 0 & pause == true) {
                        graph_ships(graphic, ImageIO.read(new File("images/nave.png")));

                        get_bonus();

                        for (int i = 0; i < c_bs; i++) {
                            graphic.setColor(Color.cyan);
                            graphic.fillRect(x_bs[i], y_bs[i], h_bullet / 2, h_bullet);
                            y_bs[i] -= v_bs;
                            if (y_bs[i] <= 0) {
                                x_bs[i] = -200;
                            }
                        }

                        for (int i = 0; i < c_bi; i++) {
                            graphic.setColor(Color.yellow);
                            graphic.fillRect(x_bi[i], y_bi[i], h_bullet / 2, h_bullet);
                            y_bi[i] += v_bi;
                        }

                        tf_level += System.currentTimeMillis() - ti_level;
                        if (tf_level >= 3000) {
                            pause = false;
                            tf_level = 0;
                        }
                        repaint();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Pantalla.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        add(panel);
        show_text(text_score, 1, 25, Color.RED);
        show_text(text_level, 1, 100, Color.GREEN);
        for (int i = 0; i < 30; i++) {
            JLabel vacio = new JLabel("                                         ");
            panel.add(vacio);
        }
        show_text(text_death1, 1, 100, Color.RED);
        show_text(text_death2, 1, 100, Color.RED);
        text_death1.setVisible(false);
        text_death2.setVisible(false);
    }

    /**
     * Método principal.
     *
     * @param args
     */
    public static void main(String[] args) {
        Pantalla pantalla = new Pantalla();
    }

    /**
     * @param tecla
     */
    @Override
    public void keyTyped(KeyEvent tecla) {
    }

    /**
     * Método para mover la nave con las flechas horizontales.
     *
     * @param tecla;
     */
    @Override
    public void keyPressed(KeyEvent tecla) {

        if (lives != 0 | deaths != n | pause != true) {
            if (tecla.getKeyCode() == 39) {
                x_ship += v_ship;
                x_shipL += v_ship;
                x_shipR += v_ship;
            }
            if (tecla.getKeyCode() == 37) {
                x_ship -= v_ship;
                x_shipL -= v_ship;
                x_shipR -= v_ship;
            }
            if (x_ship >= b) {
                x_shipR = x_shipL - b;
            }
            if (x_shipL >= b) {
                x_ship = x_shipR - b;
            }
            if (x_shipR >= b) {
                x_shipL = x_ship - b;
            }
            if (x_ship <= 0) {
                x_shipL = x_shipR + b;
            }
            if (x_shipL <= 0) {
                x_shipR = x_ship + b;
            }
            if (x_shipR <= 0) {
                x_ship = x_shipL + b;
            }
            repaint();
        }
    }

    /**
     * Método para disparar con la tecla espaciadora.
     *
     * @param tecla
     */
    @Override
    public void keyReleased(KeyEvent tecla) {
        if (shot == true) {
            if (tecla.getKeyCode() == 32) {
                int position = x_ship;
                if (x_shipL + b_ship / 2 >= 0 & x_shipL - b_ship / 2 <= b) {
                    position = x_shipL;
                } else if (x_ship + b_ship / 2 >= 0 & x_ship - b_ship / 2 <= b) {
                    position = x_ship;
                } else if (x_shipR + b_ship / 2 >= 0 & x_shipR - b_ship / 2 <= b) {
                    position = x_shipR;
                }

                x_bs[c_bs] = position + (b_ship / 2) - ((h_bullet / 2) / 2);
                y_bs[c_bs] = y_ship - h_bullet;

                c_bs++;
                sound("images/shot.wav");
                repaint();
            }
        }
    }

    public void revive_invaders() {
        for (int i = 0; i < alive.length; i++) {
            alive[i] = true;
        }
    }

    /**
     * Método para ganar vidas al chocar un gráfico de bonus.
     */
    public void get_bonus() {
        Rectangle bono = new Rectangle(x_storm, y_storm, b_storm, h_storm);
        Rectangle real = new Rectangle(x_ship, y_ship, b_ship, h_ship);
        Rectangle left = new Rectangle(x_shipL, y_ship, b_ship, h_ship);
        Rectangle right = new Rectangle(x_shipR, y_ship, b_ship, h_ship);

        if (bono.intersects(real) | bono.intersects(left) | bono.intersects(right)) {
            x_storm = -(2 * b);
            y_storm = h + h_ship;
            lives++;
            score += 15;
            sound("images/bonus.wav");
        }
    }

    /**
     * Método para asignar una serie de características a una etiqueta.
     *
     * @param label
     * @param type
     * @param length
     * @param color
     */
    public void show_text(JLabel label, int type, int length, Color color) {
        // Para type: PLAIN = 0; BOLD = 1; ITALIC = 2.
        Font font = new Font("Free Pixel", type, length);
        label.setFont(font);
        label.setForeground(color);
        panel.add(label);
    }

    /**
     * Método para cargar imágenes
     *
     * @param path
     * @return image
     * @throws IOException
     */
    public BufferedImage charge_image(String path) throws IOException {
        BufferedImage image = ImageIO.read(new File(path));
        return image;
    }

    /**
     * Método para graficar la nave de juego
     *
     * @param grafico
     * @param nave
     */
    public void graph_ships(Graphics grafico, BufferedImage nave) {
        grafico.drawImage(nave, x_ship, y_ship, b_ship, h_ship, null);
        grafico.drawImage(nave, x_shipL, y_ship, b_ship, h_ship, null);
        grafico.drawImage(nave, x_shipR, y_ship, b_ship, h_ship, null);
    }

    /**
     * Método para graficar las vidas del jugador.
     *
     * @param graph
     * @param image
     */
    public void lives(Graphics graph, BufferedImage image) {
        int x_mini = 15; // posición de vida en x
        int y_mini = 15; // posición de vida en y
        int b_mini = 38; // base de vidas
        int h_mini = 40; // altura de vidas
        int _mini = 7; // espacio entre vidas
        for (int i = 1; i <= lives; i++) {
            if (i % 4 == 0) {
                y_mini += h_mini + _mini;
                x_mini = x_mini - 3 * (b_mini + _mini);
            }
            graph.drawImage(image, x_mini, y_mini, b_mini, h_mini, null);
            x_mini += _mini + b_mini;
        }
    }

    /**
     * Método para actualizar las variables de los niveles.
     */
    public void update_level() {
        level++;
        text_level.setText("Nivel " + level);

        if (level % 2 == 0 & y_storm > h) {
            x_storm = azar.nextInt(b - b_storm);
            y_storm = -1000;
        }

        vx_invader += 5;
        vy_invader += 5;
        v_bs += 5;
        v_ship += 5;

        if (frequency >= 10) {
            frequency -= 5;
        }
        if (tf_fall >= 10) {
            tf_fall -= 5;
        }

        if (level % 2 == 0) {
            columns++;
        } else if (level % 3 == 0) {
            rows++;
        }

        if ((columns + 1) * (b_invader + _invaders) >= b) {
            b_invader = (int) (b_invader / 2);
            h_invader = (int) (h_invader / 2);
            b_ship = (int) (b_ship / 2);
            h_ship = (int) (h_ship / 2);
            h_bullet = (int) (h_bullet / 2);
        }

        n = rows * columns;
        x_invader = new int[n + 1];
        y_invader = new int[n + 1];
        x_invaderF = new int[n + 1];
        y_invaderF = new int[n + 1];
        alive = new boolean[n];
        for (int i = 0; i < alive.length; i++) {
            alive[i] = true;
        }

        x_invader[0] = 0;
        x_invaderF[0] = x_invader[0];

        yo_invaders = re_invaders;
        yo_invadersF = -((h_invader + _invaders) * rows);
        re_invadersF = yo_invadersF;
        counter = 0;
        color = 0;
    }

    /**
     * Método para insertar el sonido de un clip de audio.
     *
     * @param ruta
     */
    public void sound(String ruta) {
        try {
            AudioInputStream audio;
            audio = AudioSystem.getAudioInputStream(new File(ruta).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(Pantalla.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}