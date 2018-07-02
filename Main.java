package cambioDeManosDeCalles;

import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		Cambio c = new Cambio("cambio_sinCambios.in");
		c.resolver();
	}

}
