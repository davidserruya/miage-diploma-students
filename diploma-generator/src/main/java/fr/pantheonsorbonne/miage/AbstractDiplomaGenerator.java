package fr.pantheonsorbonne.miage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Collection;



import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import java.util.logging.Logger;
import com.itextpdf.text.pdf.PdfWriter;

import fr.pantheonsorbonne.miage.diploma.DiplomaSnippet;

public abstract class AbstractDiplomaGenerator implements DiplomaGenerator {



	public AbstractDiplomaGenerator() {
		super();
		

	}

	/**
	 * provides all the snippets used for the concrete diploma implementation
	 * 
	 * @return
	 */
	protected abstract  Collection<DiplomaSnippet> getDiplomaSnippets();

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.pantheonsorbonne.miage.DiplomaGenerator#getContent()
	 */
	@Override 
	public InputStream getContent()  {
		Logger log1 = Logger.getLogger("fr.pantheonsorbonne.miage.getContent");

		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {

			this.writeToStream(bos);

			return new ByteArrayInputStream(bos.toByteArray());

		} catch (IOException e) {

			log1.log(null,"failed to generate the file to stream to", e);
		}
		return null;

	}

	protected void writeToStream(OutputStream os) {
		Document document = new Document();
		Logger log1 = Logger.getLogger("fr.pantheonsorbonne.miage.getContent");
	
		try {

			Path image = new File("src/main/resources/diploma.png").toPath();
			Rectangle rect = new Rectangle(800f, 600f);
			document.setPageSize(rect);

			PdfWriter writer = PdfWriter.getInstance(document, os);
			document.open();

			for (DiplomaSnippet snippet : this.getDiplomaSnippets()) {
				snippet.write(writer);
			}

			document.add(Image.getInstance(image.toAbsolutePath().toString()));

		} catch (DocumentException | IOException e) {
			log1.log(null,"failed to generate the file to stream to", e);
		} finally {
			document.close();
		}
	}

}