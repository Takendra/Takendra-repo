import logging
import sys
import os

def get_logger(filename):
    logger = logging.getLogger(filename)
    logger.setLevel(logging.DEBUG)

    desktop_logs = os.path.join(os.path.join(os.path.expanduser('~')), 'Desktop', 'logs')
    if not os.path.exists(desktop_logs):
        os.mkdir(desktop_logs)

    c_handler = logging.StreamHandler(sys.stdout)
    f_handler = logging.FileHandler("{location}/{filename}.log".format(location=desktop_logs, filename=filename))

    c_handler.setLevel(logging.DEBUG)
    f_handler.setLevel(logging.DEBUG)

    # Create formatters and add it to handlers
    c_format = logging.Formatter('%(name)s - %(message)s')
    f_format = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')

    c_handler.setFormatter(c_format)
    f_handler.setFormatter(f_format)

    logger.addHandler(c_handler)
    logger.addHandler(f_handler)

    return logger