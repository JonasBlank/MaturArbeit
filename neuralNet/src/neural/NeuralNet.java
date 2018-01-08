package neural;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NeuralNet
{
	private int numInput;
	private int numHidden;
	private int numOutput;

	private double[] hInputs;
	private double[] hOutputs;
	private double[] oInputs;
	private double[] oOutputs;

	private double[][] ihWeights;
	private double[][] hoWeights;

	private double[][] trainInput;
	private double[][] trainOutput;

	private double hBias;
	private double oBias;

	private boolean print;
	
	private final int max_iterations;
	private final double learning_rate;

	public NeuralNet(int input, int hidden, int output, double[][] trainData, boolean print, int maxIt, double learnRate)
	{
		numInput = input;
		numHidden = hidden;
		numOutput = output;

		max_iterations = maxIt;
		learning_rate = learnRate;
		
		hInputs = new double[numHidden];
		hOutputs = new double[numHidden];
		oInputs = new double[numOutput];
		oOutputs = new double[numOutput];

		ihWeights = new double[numInput][numHidden];
		hoWeights = new double[numHidden][numOutput];

		trainInput = new double[trainData.length][numInput];
		trainOutput = new double[trainData.length][numOutput];

		hBias = randomize();
		oBias = randomize();

		ihWeights = randomize(ihWeights);
		hoWeights = randomize(hoWeights);

		this.print = print;

		for (int i = 0; i < trainData.length; i++)
		{
			System.arraycopy(trainData[i], 0, trainInput[i], 0, numInput);
			System.arraycopy(trainData[i], numInput, trainOutput[i], 0, numOutput);
		}

	}

	private double randomize()
	{
		return Math.random();
	}

	private double[][] randomize(double[][] d)
	{
		for (int i = 0; i < d.length; i++)
			for (int k = 0; k < d[i].length; k++)
				d[i][k] = randomize();
		return d;
	}

	public void trainNetwork()
	{
		int iterations = 0;
		while (iterations < max_iterations)
		{
			iterations++;

			List<Integer> idxs = new ArrayList<Integer>();
			for (int i = 0; i < trainInput.length; i++)
				idxs.add(i);

			Collections.shuffle(idxs);

			long starttime = System.currentTimeMillis();
			
			for (int k = 0; k < trainInput.length; k++)
			{
				int l = idxs.get(k);
				// int l = k;


				/**
				 * Forward Propagation
				 */

				// Calc hidden Inputs and Outputs
				for (int h = 0; h < numHidden; h++)
				{
					double sum = 0;
					for (int i = 0; i < numInput; i++)
					{
						sum += ihWeights[i][h] * trainInput[l][i];
					}
					sum += hBias;

					sum /= numHidden;
					
					hInputs[h] = sum;
					hOutputs[h] = MainNeural.activationFunction(sum);
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
					oOutputs[o] = MainNeural.activationFunction(sum);
				}

				/**
				 * Backwards Propagation
				 */

				double totalError = 0;
				for (int o = 0; o < numOutput; o++)
				{
					totalError += Math.pow((0.5 * (trainOutput[l][o] - oOutputs[o])), 2);
				}


				// Calc output Delta
				double[] oDelta = new double[numOutput];
				double[] hDelta = new double[numHidden];

				double[][] hoWeightsUpdate = new double[numHidden][numOutput];
				double[][] ihWeightsUpdate = new double[numInput][numHidden];

				for (int o = 0; o < numOutput; o++)
				{
					oDelta[o] = -(trainOutput[l][o] - oOutputs[o]) * MainNeural.derivativeActivationFunction(oInputs[o]);
				}

				// Calc Weight Adjustment for Hidden-Input Weights
				for (int o = 0; o < numOutput; o++)
				{
					for (int h = 0; h < numHidden; h++)
					{
						hoWeightsUpdate[h][o] = -learning_rate * oDelta[o] * hOutputs[h];
					}
				}

				// Calc hidden Delta
				for (int h = 0; h < numHidden; h++)
				{
					double sum = 0;
					for (int o = 0; o < numOutput; o++)
					{
						sum += oDelta[o] * hoWeights[h][o];
					}
					hDelta[h] = sum * MainNeural.derivativeActivationFunction(hInputs[h]);
				}

				// Calc Weight Adjustment for Input-Hidden Weights
				for (int h = 0; h < numHidden; h++)
				{
					for (int i = 0; i < numInput; i++)
					{
						ihWeightsUpdate[i][h] = -learning_rate * hDelta[h] * trainInput[l][i];
					}
				}

				// Edit Weights and Biases
				for (int o = 0; o < numOutput; o++)
				{
					for (int h = 0; h < numHidden; h++)
					{
						hoWeights[h][o] += hoWeightsUpdate[h][o];
					}
					oBias += -learning_rate * oDelta[o];
				}
				for (int h = 0; h < numHidden; h++)
				{
					for (int i = 0; i < numInput; i++)
					{
						ihWeights[i][h] += ihWeightsUpdate[i][h];
					}
					hBias += -learning_rate * hDelta[h];
				}

			}
			
			if (iterations % 100 == 0)
			{
				double acc = testAccuracy();
				System.out.println(acc);
				
				if (print)
					MainNeural.printIntoFile("data/" + max_iterations + "/" + learning_rate + "/hidden/" + "accuracy_" + numHidden + ".csv", iterations +  ";" + acc, true);
				
				if(1 - acc < 0.0001)
				{	
					System.out.println(Arrays.deepToString(hoWeights));
					System.out.println(Arrays.deepToString(ihWeights));

					break;
				}
			}
			
			if (print)
				MainNeural.printIntoFile("data/" + max_iterations + "/" + learning_rate + "/hidden/" + "learntime_" + numHidden + ".csv", iterations +  ";" + (System.currentTimeMillis() - starttime), true);
			
		
			

		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("INPUT-HIDDEN WEIGHTS");
		sb.append("\n");
		for (int i = 0; i < numInput; i++)
		{
			for (int h = 0; h < numHidden; h++)
				sb.append(ihWeights[i][h] + ";");

			sb.append("\n");
		}

		sb.append("\n");
		sb.append("HIDDEN-OUTPUT WEIGHTS");
		sb.append("\n");

		for (int h = 0; h < numHidden; h++)
		{
			for (int o = 0; o < numOutput; o++)
				sb.append(hoWeights[h][o] + ";");

			sb.append("\n");
		}

		sb.append("\n");
		sb.append("HIDDEN-BIAS");
		sb.append("\n");
		sb.append(hBias);
		sb.append("\n");

		sb.append("\n");
		sb.append("OUTPUT-BIAS");
		sb.append("\n");
		sb.append(oBias);
		sb.append("\n");

		if (print)
			MainNeural.printIntoFile("data/" + max_iterations + "/" + learning_rate + "/hidden/" + "weights_" + numHidden + ".csv", sb.toString(), false);
		
	}

	public double testAccuracy()
	{
		List<Integer> idxs = new ArrayList<Integer>();
		for (int i = 0; i < trainInput.length; i++)
			idxs.add(i);

		Collections.shuffle(idxs);

		int numRight = 0;
		int numWrong = 0;

		for (int k = 0; k < trainInput.length; k++)
		{
			// int l = k;
			int l = idxs.get(k);

			double[] inputSet = trainInput[l];
			double[] outputSet = trainOutput[l];

			for (int h = 0; h < numHidden; h++)
			{
				double sum = 0;
				for (int i = 0; i < numInput; i++)
				{
					sum += ihWeights[i][h] * inputSet[i];
				}
				sum += hBias;

				sum /= numHidden;
				
				hInputs[h] = sum;
				hOutputs[h] = MainNeural.activationFunction(sum);
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
				oOutputs[o] = MainNeural.activationFunction(sum);
			}

			int r = 0;

			for (int o = 0; o < oOutputs.length; o++)
			{
				int result = (int) Math.round((oOutputs[o]));
				int ideal = (int) outputSet[o];
				if (result == ideal)
					r++;

			}

			if (r == 9)
				numRight++;
			else
				numWrong++;
		}

		return numWrong != 0 ? ((double) numRight / (double)(numRight + numWrong)) : numRight;
	}
}
