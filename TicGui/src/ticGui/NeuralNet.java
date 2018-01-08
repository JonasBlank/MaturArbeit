package ticGui;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class NeuralNet {
	private int numInput;
	private int numHidden;
	private int numOutput;

	private double[] hInputs;
	private double[] hOutputs;
	private double[] oInputs;
	private double[] oOutputs;

	private double[][] ihWeights;
	private double[][] hoWeights;

	private double hBias;
	private double oBias;

	public static final URL WEIGHTS_FILE_O = TicGui.class.getResource("/resources/data/weights_109.csv");
	public static final URL WEIGHTS_FILE_X = TicGui.class.getResource("/resources/data/weights_109.csv");

	public static final int INPUT = 9;
	public static final int HIDDEN = 109;
	public static final int OUTPUT = 9;

	public NeuralNet(int input, int hidden, int output, double[][] ihWeights, double[][] hoWeights, double hBias, double oBias)
	{
		numInput = input;
		numHidden = hidden;
		numOutput = output;

		this.ihWeights = ihWeights;
		this.hoWeights = hoWeights;

		this.hBias = hBias;
		this.oBias = oBias;

		hInputs = new double[numHidden];
		hOutputs = new double[numHidden];
		oInputs = new double[numOutput];
		oOutputs = new double[numOutput];
	}

	public int[] calc(int[] input)
	{
		for (int h = 0; h < numHidden; h++)
		{
			double sum = 0;
			for (int i = 0; i < numInput; i++)
			{
				sum += ihWeights[i][h] * input[i];
			}
			sum += hBias;

			sum /= numHidden;

			hInputs[h] = sum;
			hOutputs[h] = activationFunction(sum);
		}

		// Calc output Inputs and Outputs
		for (int o = 0; o < numOutput; o++)
		{
			double sum = 0;
			for (int h = 0; h < numHidden; h++)
			{
				sum += hoWeights[h][o] * hOutputs[h];
			}
			sum += oBias;

			sum /= numOutput;

			oInputs[o] = sum;
			oOutputs[o] = activationFunction(sum);
		}
		int[] ret = new int[oOutputs.length];
		for (int i = 0; i < oOutputs.length; i++)
		{
			ret[i] = (int) Math.round(oOutputs[i]);
		}

		return ret;
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

	public static Object readWeights(boolean ih, boolean bias, boolean x)
	{
		try
		{
			List<String> full = null;
			if (!x)
				full = Files.readAllLines(Paths.get(TicGui.getURI(WEIGHTS_FILE_O)));
			else
				full = Files.readAllLines(Paths.get(TicGui.getURI(WEIGHTS_FILE_X)));

			for (int p = 0; p < full.size(); p++)
			{
				String s = full.get(p);
				if (s.equals("INPUT-HIDDEN WEIGHTS") && ih && !bias)
				{
					double[][] ihWeights = new double[INPUT][HIDDEN];

					for (int i = 1; i <= INPUT; i++)
					{
						String line = full.get(p + i);
						for (int h = 0; h < HIDDEN; h++)
						{
							if (line.contains(";"))
							{
								String weight = line.substring(0, line.indexOf(";"));
								double d = Double.parseDouble(weight);
								line = line.replace(weight + ";", "");
								ihWeights[i - 1][h] = d;
							}
						}
					}

					return ihWeights;
				}

				if (s.equals("HIDDEN-OUTPUT WEIGHTS") && !ih && !bias)
				{
					double[][] hoWeights = new double[HIDDEN][OUTPUT];

					for (int h = 1; h <= HIDDEN; h++)
					{
						String line = full.get(p + h);
						for (int o = 0; o < OUTPUT; o++)
						{
							if (line.contains(";"))
							{
								String weight = line.substring(0, line.indexOf(";"));
								double d = Double.parseDouble(weight);
								line = line.replace(weight + ";", "");
								hoWeights[h - 1][o] = d;
							}
						}
					}

					return hoWeights;
				}

				if (s.equals("HIDDEN-BIAS") && ih && bias)
				{
					String line = full.get(p + 1);
					double d = Double.parseDouble(line);
					return d;
				}

				if (s.equals("OUTPUT-BIAS") && !ih && bias)
				{
					String line = full.get(p + 1);
					double d = Double.parseDouble(line);
					return d;
				}

			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
