package neural;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MainNeural
{

	public static final double X = 1;
	public static final double O = 2;
	public static final double EMPTY = 0;

	public static final double LEARNING_RATE = 0.5;
	public static final int MAX_ITERATIONS = 2000;

	public static final String STATE_FILE = "o-turn.txt";
	public static final String OUTPUT_STATE_FILE = "o-turn-output.txt";

	public static final int INPUT = 9;
	public static final int HIDDEN = 30;
	public static final int OUTPUT = 9;

	public static final boolean PRINT = true;

	public static void main(String[] args)
	{
		double[][] trainData = readStates();

		for (int max_it = 3500; max_it < 5000; max_it += 500)
		{
			for (double learningRate = 0.1; learningRate < 0.6; learningRate += 0.1)
			{
				for (int hiddens = 0; hiddens < 300; hiddens++)
				{
					File f = new File("data/" + max_it + "/" + learningRate + "/hidden/");
					if (!f.exists())
						f.mkdirs();

					System.out.println("Starting network with " + INPUT + " Input Neurons, " + hiddens + " Hidden Neurons and " + OUTPUT + " Output Neurons");

					NeuralNet net = new NeuralNet(INPUT, hiddens, OUTPUT, trainData, PRINT, max_it, learningRate);
					net.trainNetwork();

					double acc = net.testAccuracy();
					System.out.println("Final accuracy");
					System.out.println(acc);

					if (PRINT)
						MainNeural.printIntoFile("data/" + max_it + "/" + learningRate + "/hidden/" + "accuracy_" + hiddens + ".csv", "Final Accuracy" + ";" + acc, true);
				}
			}
		}
	}

	public static double activationFunction(double d)
	{
		return sigmoid(d);
	}

	public static double derivativeActivationFunction(double d)
	{
		return derivativeSigmoid(d);
	}

	public static double sigmoid(double x)
	{
		return 2 / (1 + Math.exp(-x));
	}

	public static double derivativeSigmoid(double x)
	{
		return (2 * Math.exp(x)) / Math.pow((Math.exp(x) + 1), 2);
	}

	public static void printIntoFile(String file, String string, boolean append)
	{
		PrintWriter printer = null;
		try
		{
			printer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), StandardCharsets.UTF_8)));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		printer.println(string);
		printer.close();
	}

	private static double[][] readStates()
	{

		try
		{
			List<String> input = Files.readAllLines(Paths.get(STATE_FILE));
			List<String> output = Files.readAllLines(Paths.get(OUTPUT_STATE_FILE));

			double[][] result = new double[input.size()][INPUT + OUTPUT];

			for (int i = 0; i < input.size(); i++)
			{
				String in = input.get(i);
				String out = output.get(i);

				char[] chIn = in.toCharArray();
				char[] chOut = out.toCharArray();

				for (int c = 0; c < chIn.length; c++)
				{
					if (chIn[c] == 'X')
						result[i][c] = X;

					if (chIn[c] == 'O')
						result[i][c] = O;

					if (chIn[c] == ' ')
						result[i][c] = EMPTY;
				}

				for (int o = 0; o < chIn.length; o++)
				{
					if (chOut[o] == 'X')
						result[i][o + INPUT] = X;

					if (chOut[o] == 'O')
						result[i][o + INPUT] = O;

					if (chOut[o] == ' ')
						result[i][o + INPUT] = EMPTY;
				}
			}

			return result;

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;

	}
}
