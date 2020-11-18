package com.craftingdead.immerse.client.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class LoggingErrorHandler implements ErrorHandler {

  public static final LoggingErrorHandler INSTANCE = new LoggingErrorHandler();

  private static final Logger logger = LogManager.getLogger();

  private LoggingErrorHandler() {}

  @Override
  public void warning(SAXParseException exception) throws SAXException {
    logger.warn(exception);
  }

  @Override
  public void error(SAXParseException exception) throws SAXException {
    logger.error(exception);

  }

  @Override
  public void fatalError(SAXParseException exception) throws SAXException {
    logger.fatal(exception);
  }
}
