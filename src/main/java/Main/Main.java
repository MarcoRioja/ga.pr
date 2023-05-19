package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DB.DB;
import Entities.PlayerE;
import Frames.GameFrame;

public class Main {

	static DB conDB;

	public static void main(String[] args) {

		try {
			conDB = new DB();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		GameFrame gameFrame = null;

		if (args.length > 0) {
			try {
				PlayerE player = new PlayerE((byte) 10, (byte) 1, (short) 480, "Marco", (byte) 0, null, null);
				gameFrame = new GameFrame(Byte.parseByte(args[0].substring(0, 1)), player, conDB);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				PlayerE player = new PlayerE((byte) 10, (byte) 1, (short) 480, "Marco", (byte) 0, null, null);
				gameFrame = new GameFrame((byte) 0, player, conDB);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		gameFrame.setLocationRelativeTo(null); // Centra el JFrame en la pantalla

		gameFrame.pack();
		gameFrame.setSize((30 * 32) + 14, (21 * 32) + 4);
		gameFrame.setVisible(true);
		
	}

	public static void uploadStage(byte nStageId, String stageName) throws SQLException, IOException {
		String terrainStage = "resources/uploadTerrainStage.txt";
		String entityStage = "resources/uploadEntityStage.txt";

		byte[][] matrixT = readMatrixFromFile(terrainStage);
		byte[][] matrixE = readMatrixFromFile(entityStage);

		conDB.uploadStage(nStageId, stageName, matrixT, matrixE);

	}

	public static byte[][] readMatrixFromFile(String filePath) throws IOException {
		List<List<Byte>> matrixList = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				List<Byte> row = new ArrayList<>();
				String[] values = line.split(",");

				for (String value : values) {
					byte byteValue = Byte.parseByte(value.trim());
					row.add(byteValue);
				}

				matrixList.add(row);
			}
		}

		int rows = matrixList.size();
		int cols = matrixList.get(0).size();
		byte[][] matrix = new byte[rows][cols];

		for (int i = 0; i < rows; i++) {
			List<Byte> row = matrixList.get(i);
			for (int j = 0; j < cols; j++) {
				matrix[i][j] = row.get(j);
			}
		}

		return matrix;
	}

}
