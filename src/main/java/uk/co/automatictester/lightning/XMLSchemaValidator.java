package uk.co.automatictester.lightning;

import com.beust.jcommander.ParameterException;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

public class XMLSchemaValidator {

    public static void validate(String xml) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File("src/main/resources/lightning.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xml)));
        } catch (SAXException e) {
            throw new ParameterException("XML file is not valid: " + e.getMessage());
        } catch (IOException e) {
            throw new ParameterException("Error accessing " + xml + " for schema validation");
        }
    }
}